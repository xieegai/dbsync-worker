package com.github.bailaohe.dbsync.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowChanged {
    private Map<String, String> preUpdate;
    private Map<String, String> snapshot;
}
