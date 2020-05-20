package com.github.bailaohe.dbsync.subscribe;

import com.github.bailaohe.dbsync.event.DBSyncAppEvent;
import com.jiejing.dbsync.event.payload.RowBatchChanged;
import com.jiejing.dbsync.subscribe.DBSyncProcessor;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;


/**
 * Created by baihe on 2017/8/24.
 */
@AllArgsConstructor
public class DBSyncAppListener {

    private DBSyncProcessor dbSyncProcessor;

    @EventListener
    public void processAppSyncEvent(DBSyncAppEvent event) {
        RowBatchChanged payload = event.getPayload();

        String interest = payload.getDb() + "." + payload.getTable();

        dbSyncProcessor.processDBSync(interest, event.getPayload(), event.getOldRows(), event.getNewRows());
    }
}
