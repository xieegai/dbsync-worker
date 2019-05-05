package com.github.bailaohe.dbsync.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBChangePayload {
    private Class<?> entityClass;
    private RowBatchChanged rowBatchChanged;
    private List oldRows;
    private List newRows;
}
