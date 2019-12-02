package com.github.bailaohe.dbsync.subscribe;

import com.github.bailaohe.dbsync.event.DBSyncAppEvent;
import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by baihe on 2017/8/24.
 */
@AllArgsConstructor
public class DBSyncListener {

    private DBSyncHandlerRegistry handlerRegistry;
    private Set<String> tableWhiteSet;

    @EventListener
    public void processAppSyncEvent(DBSyncAppEvent event) {
        RowBatchChanged payload = event.getPayload();
        List oldRows = event.getOldRows();
        List newRows = event.getNewRows();

        String interest = payload.getDb() + "." + payload.getTable();

        if (CollectionUtils.isEmpty(tableWhiteSet) || tableWhiteSet.contains(interest)) {
            if (handlerRegistry.containsKey(interest)) {
                List<IDBSyncHandler> handlers = handlerRegistry.get(interest);

                handlers.forEach(handler -> handler.onChange(payload, oldRows, newRows));
            }
        }
    }
}
