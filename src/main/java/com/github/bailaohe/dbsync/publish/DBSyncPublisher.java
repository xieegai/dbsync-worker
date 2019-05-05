package com.github.bailaohe.dbsync.publish;

import com.github.bailaohe.dbsync.event.DBSyncAppEvent;
import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import com.github.bailaohe.repository.sync.IDBSyncPublisher;
import lombok.Setter;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class DBSyncPublisher extends ApplicationObjectSupport implements IDBSyncPublisher {

    @Setter
    private ThreadPoolExecutor executor;

    @Override
    public <T> void publishInsert(Class<T> entityClass, String schema, String table, List<T> models, boolean async) {

        if (async && null != executor) {
            executor.submit(() -> {
                publishInsertInternal(entityClass, schema, table, models);
            });
        } else {
            publishInsertInternal(entityClass, schema, table, models);
        }
    }

    private <T> void publishInsertInternal(Class<T> entityClass, String schema, String table, List<T> models) {
        RowBatchChanged payload = RowBatchChanged.builder()
                .db(schema)
                .table(table)
                .eventType("INSERT")
                .ts(System.currentTimeMillis())
                .build();

        DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, models, models);
        getApplicationContext().publishEvent(event);
    }

    @Override
    public <T> void publishDelete(Class<T> entityClass, String schema, String table, List<T> models, boolean async) {
        if (async && null != executor) {
            executor.submit(() -> {
                publishDeleteInternal(entityClass, schema, table, models);
            });
        } else {
            publishDeleteInternal(entityClass, schema, table, models);
        }
    }

    private <T> void publishDeleteInternal(Class<T> entityClass, String schema, String table, List<T> models) {
        RowBatchChanged payload = RowBatchChanged.builder()
                .db(schema)
                .table(table)
                .eventType("DELETE")
                .ts(System.currentTimeMillis())
                .build();

        DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, models, models);
        getApplicationContext().publishEvent(event);
    }

    @Override
    public <T> void publishUpdate(Class<T> entityClass, String schema, String table, List<T> oldModels, List<T> newModels, boolean async) {
        if (async && null != executor) {
            executor.submit(() -> {
                publishUpdateInternal(entityClass, schema, table, oldModels, newModels);
            });
        } else {
            publishUpdateInternal(entityClass, schema, table, oldModels, newModels);
        }
    }

    private <T> void publishUpdateInternal(Class<T> entityClass, String schema, String table, List<T> oldModels, List<T> newModels) {
        RowBatchChanged payload = RowBatchChanged.builder()
                .db(schema)
                .table(table)
                .eventType("UPDATE")
                .ts(System.currentTimeMillis())
                .build();

        DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, oldModels, newModels);
        getApplicationContext().publishEvent(event);
    }
}
