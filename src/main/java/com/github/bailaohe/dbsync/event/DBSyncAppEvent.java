package com.github.bailaohe.dbsync.event;

import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class DBSyncAppEvent extends ApplicationEvent {

    @Getter
    private RowBatchChanged payload;

    @Getter
    private List oldRows;

    @Getter
    private List newRows;

    public DBSyncAppEvent(Object source, RowBatchChanged payload, List oldRows, List newRows) {
        super(source);
        this.payload = payload;
        this.oldRows = oldRows;
        this.newRows = newRows;
    }
}
