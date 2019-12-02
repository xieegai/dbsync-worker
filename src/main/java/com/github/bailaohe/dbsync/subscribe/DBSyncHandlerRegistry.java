package com.github.bailaohe.dbsync.subscribe;

import com.google.common.collect.ArrayListMultimap;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by baihe on 2017/8/24.
 */
public class DBSyncHandlerRegistry extends ApplicationObjectSupport {
    private ArrayListMultimap<String, IDBSyncHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = ArrayListMultimap.create();
        Map<String, IDBSyncHandler> beanMap = getApplicationContext().getBeansOfType(IDBSyncHandler.class);
        beanMap.values().forEach(x -> {
            Set<String> labels = x.subscribeLabels();
            for (String label: labels) {
                handlerMap.put(label, x);
            }
        });
    }

    public List<IDBSyncHandler> get(String interest) {
        return handlerMap.get(interest);
    }

    public boolean containsKey(String interest) {
        return handlerMap.containsKey(interest);
    }
}
