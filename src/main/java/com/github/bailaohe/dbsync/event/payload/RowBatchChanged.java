package com.github.bailaohe.dbsync.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private List<RowChanged> data;

    public String getLabel() {
        return db + "." + table;
    }

    public Set<String> columnsChanged() {
        return data.stream().map(x -> x.getColumnsChanged()).flatMap(Collection::stream).distinct().collect(Collectors.toSet());
    }
}
