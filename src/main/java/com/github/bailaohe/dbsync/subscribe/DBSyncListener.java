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

    private DBSyncProcessor dbSyncProcessor;
//    private Set<String> tableWhiteSet;

    @EventListener
    public void processAppSyncEvent(DBSyncAppEvent event) {
        RowBatchChanged payload = event.getPayload();

        String interest = payload.getDb() + "." + payload.getTable();

        dbSyncProcessor.processDBSync(interest, event.getPayload(), event.getOldRows(), event.getNewRows());
//        if (CollectionUtils.isEmpty(tableWhiteSet) || tableWhiteSet.contains(interest)) {
//            dbSyncProcessor.processDBSync(interest, event.getPayload(), event.getOldRows(), event.getNewRows());
//        }
    }
}
