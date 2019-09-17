package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.dbsync.event.DBSyncStreamEvent;
import com.github.bailaohe.dbsync.subscribe.DBSyncHandlerRegistry;
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

//import com.github.bailaohe.dbsync.subscribe.DBSyncController;
//import com.github.bailaohe.dbsync.event.DBSyncEvent;
//import com.github.bailaohe.dbsync.subscribe.DBInitService;

@Configuration
public class DBSyncStreamConfiguration {
    @Value("${mysql.modify.subscribe:}")
    private String whiteListStr;

    @Bean
    @ConditionalOnMissingBean
    public Set<String> modifySubscribeSet() {
        return StringUtils.isEmpty(whiteListStr) ? Sets.newHashSet() : Arrays.stream(whiteListStr.split(",")).collect(Collectors.toSet());
    }

    @Bean
    @ConditionalOnMissingBean
    public DBSyncHandlerRegistry dmlModifyHandlerRegistry() {
        return new DBSyncHandlerRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public DBSyncStreamListener dbSyncStreamListener() {
        return new DBSyncStreamListener(dmlModifyHandlerRegistry(), modifySubscribeSet());
    }

    @Bean
    @ConditionalOnMissingBean
    public DBSyncStreamEvent dbSyncEvent() {
        DBSyncStreamEvent event = new DBSyncStreamEvent();
        return event;
    }
}
