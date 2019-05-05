package com.github.bailaohe.dbsync.publish;

import com.github.bailaohe.dbsync.event.DBSyncAppEvent;
import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import com.github.bailaohe.repository.sync.IDBSyncPublisher;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.List;

public class DBSyncPublisher extends ApplicationObjectSupport implements IDBSyncPublisher {

    @Override
    public <T> void publishInsert(Class<T> entityClass, String schema, String table, List<T> models, boolean async) {
        RowBatchChanged payload = RowBatchChanged.builder()
                .db(schema)
                .table(table)
                .eventType("INSERT")
                .ts(System.currentTimeMillis())
                .build();

        DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, models, models);
        getApplicationContext().publishEvent(event);

//        if (!async) {
//            DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, models, models);
//            getApplicationContext().publishEvent(event);
//        } else {
//            dbSyncAppAsyncEvent.trigger(
//                    DBChangePayload.builder()
//                            .entityClass(entityClass)
//                            .rowBatchChanged(payload)
//                            .oldRows(models)
//                            .newRows(models)
//                            .build());
//        }
    }

    @Override
    public <T> void publishDelete(Class<T> entityClass, String schema, String table, List<T> models, boolean async) {
        RowBatchChanged payload = RowBatchChanged.builder()
                .db(schema)
                .table(table)
                .eventType("DELETE")
                .ts(System.currentTimeMillis())
                .build();

        DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, models, models);
        getApplicationContext().publishEvent(event);

//        if (!async) {
//            DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, models, models);
//            getApplicationContext().publishEvent(event);
//        } else {
//            dbSyncAppAsyncEvent.trigger(
//                    DBChangePayload.builder()
//                            .entityClass(entityClass)
//                            .rowBatchChanged(payload)
//                            .oldRows(models)
//                            .newRows(models)
//                            .build());
//        }
    }

    @Override
    public <T> void publishUpdate(Class<T> entityClass, String schema, String table, List<T> oldModels, List<T> newModels, boolean async) {
        RowBatchChanged payload = RowBatchChanged.builder()
                .db(schema)
                .table(table)
                .eventType("UPDATE")
                .ts(System.currentTimeMillis())
                .build();

        DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, oldModels, newModels);
        getApplicationContext().publishEvent(event);

//        if (!async) {
//            DBSyncAppEvent event = new DBSyncAppEvent(entityClass, payload, oldModels, newModels);
//            getApplicationContext().publishEvent(event);
//        } else {
//            dbSyncAppAsyncEvent.trigger(
//                    DBChangePayload.builder()
//                            .entityClass(entityClass)
//                            .rowBatchChanged(payload)
//                            .oldRows(oldModels)
//                            .newRows(newModels)
//                            .build());
//        }
    }
}
