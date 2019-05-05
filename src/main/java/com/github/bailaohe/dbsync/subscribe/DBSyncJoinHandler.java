package com.github.bailaohe.dbsync.subscribe;

import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import com.google.common.collect.Maps;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract class to process a star-scheme join work on a primary subscribe and several secondary subscribes
 * @param <S> the type of the source (primary subscribe) entity
 * @param <I> the type of the key for both source and target entity
 * @param <T> the type of the target entity
 */
public abstract class DBSyncJoinHandler<S, I, T> extends DBSyncETLHandler<S, I, T> {

    private Map<String, Function<List, List<S>>> transformMap;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        transformMap = Maps.newHashMap();
        initTransformMap(transformMap);
        for (DBSyncSubscribe secondarySubscribe: secondarySubscribes()) {
            Assert.isTrue(subscribeMap().containsKey(secondarySubscribe.getLabel()),
                    String.format("No transformation found for secondary subscribe: %s", secondarySubscribe.getLabel()));
        }
    }


    @Override
    public void processSecondaryUpdate(RowBatchChanged payload, List oldSecondaryRows, List newSecondaryRows) {
        List<S> rootList = transformMap.get(payload.getLabel()).apply(newSecondaryRows);
        if (!CollectionUtils.isEmpty(rootList)) {
            processUpdate(payload, rootList, rootList);
        }
    }

    /**
     * Init the transform function Map from secondary to primary subscribe
     * @param map the map
     */
    public abstract void initTransformMap(Map<String, Function<List, List<S>>> map);
}
