//package com.github.bailaohe.dbsync.subscribe;
//
//import lombok.AllArgsConstructor;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@AllArgsConstructor
//public class DBInitService {
//
//    private DBSyncHandlerRegistry handlerRegistry;
//
//    public void initTarget(String label, Class<?> targetClass) {
//        if (handlerRegistry.containsKey(label)) {
//            List<DBSyncHandler> handlers = handlerRegistry.get(label);
//            List<DBSyncETLHandler> finalHandlers = handlers.stream().filter(x -> x instanceof DBSyncETLHandler
//                    && ((DBSyncETLHandler)x).getTargetEntityClass().equals(targetClass))
//                    .map(x -> (DBSyncETLHandler)x).collect(Collectors.toList());
//            if (!CollectionUtils.isEmpty(finalHandlers)) {
//                DBSyncETLHandler handler = finalHandlers.get(0);
//                handler.initTargetTable();
//            }
//        }
//    }
//}
