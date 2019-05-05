package com.github.bailaohe.dbsync.publish;

import java.util.List;

public interface IDBSyncPublisher {

    <T> void publishInsert(Class<T> entityClass, String schema, String table, List<T> models, boolean async);

    <T> void publishDelete(Class<T> entityClass, String schema, String table, List<T> models, boolean async);

    <T> void publishUpdate(Class<T> entityClass, String schema, String table, List<T> oldModels, List<T> newModels, boolean async);
}
