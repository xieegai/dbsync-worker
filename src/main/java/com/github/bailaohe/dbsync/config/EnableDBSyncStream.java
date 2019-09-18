package com.github.bailaohe.dbsync.config;

import com.github.bailaohe.csevent.annotation.EnableEventConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DBSyncStreamConfiguration.class)
@EnableEventConfig
public @interface EnableDBSyncStream {
}
