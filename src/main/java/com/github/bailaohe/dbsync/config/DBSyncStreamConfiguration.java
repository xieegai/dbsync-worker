package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.dbsync.event.DBSyncStreamEvent;
import com.github.bailaohe.dbsync.subscribe.DBSyncHandlerRegistry;
import com.github.bailaohe.dbsync.subscribe.DBSyncProcessor;
import com.github.bailaohe.dbsync.subscribe.DBSyncStreamListener;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DBSyncStreamConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DBSyncStreamListener dbSyncStreamListener(DBSyncProcessor dbSyncProcessor) {
        return new DBSyncStreamListener(dbSyncProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    public DBSyncStreamEvent dbSyncEvent() {
        DBSyncStreamEvent event = new DBSyncStreamEvent();
        return event;
    }
}
