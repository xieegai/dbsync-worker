package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.dbsync.publish.DBSyncPublisher;
import com.github.bailaohe.dbsync.subscribe.DBSyncAppListener;
import com.github.bailaohe.repository.sync.IDBSyncPublisher;
import com.jiejing.dbsync.subscribe.DBSyncProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class DBSyncAppConfiguration {
    @Bean
    public DBSyncAppListener dbSyncListener(DBSyncProcessor dbSyncProcessor) {
        return new DBSyncAppListener(dbSyncProcessor);
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
