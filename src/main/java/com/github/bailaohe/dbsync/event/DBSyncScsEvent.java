package com.github.bailaohe.dbsync.event;


import com.jiejing.dbsync.event.payload.RowChanged;
import com.jiejing.scs.event.annotation.EventMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author baihe
 */
@EventMeta(name = "db-sync", domain = "sync",
  description = "the event to sync database record modification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DBSyncScsEvent {
    private String eventType;
    private String db;
    private String table;
    private String logFile;
    private Long logFileOffset;
    private Long ts;
    private List<RowChanged> rows;
    private Set<String> columnsChanged;
}
