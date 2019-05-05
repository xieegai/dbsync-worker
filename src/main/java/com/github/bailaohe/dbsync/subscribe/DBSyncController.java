//package com.github.bailaohe.dbsync.subscribe;
//
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@AllArgsConstructor
//public class DBSyncController {
//
//    private DBInitService dbInitService;
//
//    @GetMapping("${xiaomai.dml.init.path:/xiaomai/dml/init}")
//    public void initTargetTable(@RequestParam String db, @RequestParam String table, @RequestParam String targetClass) {
//        try {
//            dbInitService.initTarget(db + "." + table, Class.forName(targetClass));
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
