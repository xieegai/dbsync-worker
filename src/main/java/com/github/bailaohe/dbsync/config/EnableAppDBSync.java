package com.github.bailaohe.dbsync.config;

import com.jiejing.dbsync.config.DBSyncConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DBSyncConfiguration.class, DBSyncAppConfiguration.class})
public @interface EnableAppDBSync {
}
