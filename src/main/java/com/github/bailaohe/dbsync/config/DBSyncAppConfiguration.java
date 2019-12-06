package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.dbsync.subscribe.DBSyncListener;
import com.github.bailaohe.dbsync.subscribe.DBSyncProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBSyncAppConfiguration {
    @Bean
    public DBSyncListener dbSyncListener(DBSyncProcessor dbSyncProcessor) {
        return new DBSyncListener(dbSyncProcessor);
    }
}
