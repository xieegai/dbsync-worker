package com.github.bailaohe.dbsync.subscribe;

import com.github.bailaohe.csevent.annotation.EventHandler;
import com.github.bailaohe.dbsync.event.DBSyncStreamEvent;
import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by baihe on 2017/8/24.
 */
@AllArgsConstructor
public class DBSyncStreamListener {

    private DBSyncHandlerRegistry handlerRegistry;
    private Set<String> tableWhiteSet;

    @EventHandler(DBSyncStreamEvent.class)
    public void processBinlogNotify(RowBatchChanged payload) {
        String interest = payload.getDb() + "." + payload.getTable();

        if (CollectionUtils.isEmpty(tableWhiteSet) || tableWhiteSet.contains(interest)) {
            if (handlerRegistry.containsKey(interest)) {
                List<DBSyncHandler> handlers = handlerRegistry.get(interest);

                handlers.forEach(handler -> handler.onChange(payload));
            }
        }
    }
}
