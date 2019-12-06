package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.dbsync.publish.DBSyncPublisher;
import com.github.bailaohe.dbsync.subscribe.DBSyncHandlerRegistry;
import com.github.bailaohe.dbsync.subscribe.DBSyncListener;
import com.github.bailaohe.dbsync.subscribe.DBSyncProcessor;
import com.github.bailaohe.repository.sync.IDBSyncPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class DBSyncConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DBSyncHandlerRegistry dbSyncHandlerRegistry() {
        return new DBSyncHandlerRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public DBSyncProcessor dbSyncProcessor() {
        return new DBSyncProcessor(dbSyncHandlerRegistry());
    }

    @Bean
    public DBSyncListener dbSyncListener() {
        return new DBSyncListener(dbSyncProcessor());
    }

    @Bean
    @ConditionalOnMissingBean
    public IDBSyncPublisher dbSyncPublisher() {
        DBSyncPublisher publisher = new DBSyncPublisher();
        publisher.setExecutor(new ThreadPoolExecutor(8, 32,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()));
        return publisher;
    }
}
