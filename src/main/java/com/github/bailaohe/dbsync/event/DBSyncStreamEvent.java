package com.github.bailaohe.dbsync.event;


import com.github.bailaohe.csevent.AbstractEvent;
import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;

/**
 * @author baihe
 */
public class DBSyncStreamEvent extends AbstractEvent<RowBatchChanged> {
    @Override
    public String getDescription() {
        return "Data Modified";
    }

}
