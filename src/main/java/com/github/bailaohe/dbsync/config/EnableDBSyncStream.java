package com.github.bailaohe.dbsync.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DBSyncConfiguration.class)
public @interface EnableDBSyncStream {
}