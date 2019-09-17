package com.github.bailaohe.dbsync.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowBatchChanged {
    private String eventType;
    private String db;
    private String table;
    private String logFile;
    private Long logFileOffset;
    private Long ts;
    private List<RowChanged> rows;
    private Set<String> columnsChanged;

    public String getLabel() {
        return db + "." + table;
    }
}
