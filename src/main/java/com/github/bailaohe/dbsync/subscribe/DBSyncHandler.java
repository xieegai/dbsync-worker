package com.github.bailaohe.dbsync.subscribe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import com.github.bailaohe.dbsync.event.payload.RowChanged;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class DBSyncHandler<T> {

    @Getter
    protected Class<T> subscribeEntityClass;

    /**
     * We only take care of DML change (insert/delete/update)
     */
    private static Set<String> interestedEvents = ImmutableSet.of("INSERT", "DELETE", "UPDATE");

    /**
     * The primary subscribed db
     * @return the primary subscribed db
     */
    public abstract String subscribeDb();

    /**
     * The primary subscribed table
     * @return the primary subscribed table
     */
    public abstract String subscribeTable();

    /**
     * THe primary subscribed columns
     * @return the primary subscribed columns
     */
    public Set<String> subscribeColumns() {
        return Sets.newHashSet();
    }

    @PostConstruct
    public void init() {
        Class subClass = getClass();
        while (!(subClass.getGenericSuperclass() instanceof ParameterizedType)
                && subClass.getSuperclass() != DBSyncHandler.class) {
            subClass = subClass.getSuperclass();
        }

        subscribeEntityClass = (Class<T>) ((ParameterizedType)subClass.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Init the primary subscribe with above methods
     * @return the primary subscribe
     */
    public DBSyncSubscribe subscribe() {
        return DBSyncSubscribe.builder()
                .db(subscribeDb())
                .table(subscribeTable())
                .entityClass(subscribeEntityClass)
                .columns(subscribeColumns())
                .build();
    }

    /**
     * Specify the secondary subscribe list
     * @return the secondary subscribe list
     */
    public List<DBSyncSubscribe> secondarySubscribes() {
        return ImmutableList.of();
    }

    /**
     * Generate the secondary subscribe map
     * @return the secondary subscribe map
     */
    public Map<String, DBSyncSubscribe> subscribeMap() {
        return Stream.of(ImmutableList.of(subscribe()), secondarySubscribes())
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toMap(DBSyncSubscribe::getLabel, Function.identity()));
    }

    /**
     * Generate the subscribe labels
     * @return the subscribe labels
     */
    public Set<String> subscribeLabels() {
        return Stream.of(ImmutableList.of(subscribe()), secondarySubscribes())
                .flatMap(Collection::stream)
                .distinct()
                .map(DBSyncSubscribe::getLabel)
                .collect(Collectors.toSet());
    }

    private static List parseNewRows(RowBatchChanged payload, Class<?> entityClass) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        return payload.getRows().stream().map(RowChanged::getSnapshot)
                .map(x -> JSON.parseObject(JSON.toJSONString(x, serializeConfig), entityClass))
                .collect(Collectors.toList());
    }

    private static List parseOldRows(RowBatchChanged payload, Class<?> entityClass) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        return payload.getRows().stream().map(RowChanged::getSnapshot)
                .map(x -> JSON.parseObject(JSON.toJSONString(x, serializeConfig), entityClass))
                .collect(Collectors.toList());
    }

    /**
     * Entry method when a change is detected
     * @param payload the payload of the DML change
     */
    public void onChange(RowBatchChanged payload) {
        // only process insert / delete /update
        if (!interestedEvents.contains(payload.getEventType())) {
            return;
        }

        String eventType = payload.getEventType();

        // drop data if not in subscribed tables
        DBSyncSubscribe subscribeHit = subscribeMap().get(payload.getLabel());
        if (subscribeHit == null) {
            return;
        }
        // drop data if not in subscribed columns (only for update)
        if (eventType == "UPDATE"
                && !CollectionUtils.isEmpty(subscribeHit.getColumns())
                && Collections.disjoint(subscribeHit.getColumns(), payload.getColumnsChanged())) {
            return;
        }

        List oldRows = parseOldRows(payload, subscribeHit.getEntityClass());
        List newRows = parseNewRows(payload, subscribeHit.getEntityClass());

        if (eventType.equalsIgnoreCase("INSERT")) {
            if (payload.getDb().equals(subscribe().getDb())
                    && payload.getTable().equals(subscribe().getTable())) {
                processInsert(payload, (List<T>)newRows);
            } else {
                processSecondaryInsert(payload, newRows);
            }
        } else if (eventType.equalsIgnoreCase("DELETE")) {
            if (payload.getDb().equals(subscribe().getDb())
                    && payload.getTable().equals(subscribe().getTable())) {
                processDelete(payload, (List<T>)oldRows);
            } else {
                processSecondaryDelete(payload, oldRows);
            }
        } else if (eventType.equalsIgnoreCase("UPDATE")) {
            if (payload.getDb().equals(subscribe().getDb())
                    && payload.getTable().equals(subscribe().getTable())) {
                processUpdate(payload, (List<T>)oldRows, (List<T>)newRows);
            } else {
                processSecondaryUpdate(payload, oldRows, newRows);
            }
        }
    }

    public void onChange(RowBatchChanged payload, List oldRows, List newRows) {
        // only process insert / delete /update
        if (!interestedEvents.contains(payload.getEventType())) {
            return;
        }

        String eventType = payload.getEventType();

        // drop data if not in subscribed tables
        DBSyncSubscribe subscribeHit = subscribeMap().get(payload.getLabel());
        if (subscribeHit == null) {
            return;
        }

        if (eventType.equalsIgnoreCase("INSERT")) {
            if (payload.getDb().equals(subscribe().getDb())
                    && payload.getTable().equals(subscribe().getTable())) {
                processInsert(payload, (List<T>)newRows);
            } else {
                processSecondaryInsert(payload, newRows);
            }
        } else if (eventType.equalsIgnoreCase("DELETE")) {
            if (payload.getDb().equals(subscribe().getDb())
                    && payload.getTable().equals(subscribe().getTable())) {
                processDelete(payload, (List<T>)oldRows);
            } else {
                processSecondaryDelete(payload, oldRows);
            }
        } else if (eventType.equalsIgnoreCase("UPDATE")) {
            if (payload.getDb().equals(subscribe().getDb())
                    && payload.getTable().equals(subscribe().getTable())) {
                processUpdate(payload, (List<T>)oldRows, (List<T>)newRows);
            } else {
                processSecondaryUpdate(payload, oldRows, newRows);
            }
        }
    }

    /**
     * handler function when insert rows to the primary subscribe
     * @param payload
     * @param newRows
     */
    public void processInsert(RowBatchChanged payload, List<T> newRows) {
    }

    /**
     * handler function when delete rows from the primary subscribe
     * @param payload
     * @param oldRows
     */
    public void processDelete(RowBatchChanged payload, List<T> oldRows) {
    }

    /**
     * handler function when update rows in the primary subscribe
     * @param payload
     * @param oldRows
     * @param newRows
     */
    public void processUpdate(RowBatchChanged payload, List<T> oldRows, List<T> newRows) {

    }

    /**
     * handler function when insert rows to the secondary subscribe
     * @param payload
     * @param newRows
     */
    public void processSecondaryInsert(RowBatchChanged payload, List newRows) {

    }

    /**
     * handler function when delete rows from the secondary subscribe
     * @param payload
     * @param oldRows
     */
    public void processSecondaryDelete(RowBatchChanged payload, List oldRows) {

    }

    /**
     * handler function when update rows in the secondary subscribe
     * @param payload
     * @param oldRows
     * @param newRows
     */
    public void processSecondaryUpdate(RowBatchChanged payload, List oldRows, List newRows) {

    }
}
