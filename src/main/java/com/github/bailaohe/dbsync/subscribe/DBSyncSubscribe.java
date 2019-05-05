package com.github.bailaohe.dbsync.subscribe;

import com.google.common.collect.Sets;
import lombok.*;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"db", "table"})
public class DBSyncSubscribe {
    private String db;
    private String table;
    private Class<?> entityClass;

    @Builder.Default
    private Set<String> columns = Sets.newHashSet();

    public String getLabel() {
        return this.db + "." + this.table;
    }

}
