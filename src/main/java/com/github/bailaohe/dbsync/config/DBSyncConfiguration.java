package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.dbsync.publish.DBSyncPublisher;
import com.github.bailaohe.dbsync.publish.IDBSyncPublisher;
import com.github.bailaohe.dbsync.subscribe.DBSyncHandlerRegistry;
import com.github.bailaohe.dbsync.subscribe.DBSyncListener;
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
public class DBSyncConfiguration {
    @Value("${mysql.modify.subscribe:}")
    private String whiteListStr;

    @Bean
    public Set<String> modifySubscribeSet() {
        return StringUtils.isEmpty(whiteListStr) ? Sets.newHashSet() : Arrays.stream(whiteListStr.split(",")).collect(Collectors.toSet());
    }

    @Bean
    public DBSyncHandlerRegistry dmlModifyHandlerRegistry() {
        return new DBSyncHandlerRegistry();
    }

    @Bean
    public DBSyncListener dbSyncListener() {
        return new DBSyncListener(dmlModifyHandlerRegistry(), modifySubscribeSet());
    }

    @Bean
    @ConditionalOnMissingBean
    public IDBSyncPublisher dbSyncPublisher() {
        return new DBSyncPublisher();
    }

//    @Bean
//    public DBInitService dmlInitService() {
//        return new DBInitService(dmlModifyHandlerRegistry());
//    }
//
//    @Bean
//    public DBSyncController dbSyncController() {
//        return new DBSyncController(dmlInitService());
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public DBSyncEvent dbSyncEvent() {
//        DBSyncEvent event = new DBSyncEvent();
//        return event;
//    }
}
