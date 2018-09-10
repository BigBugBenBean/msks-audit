package com.pccw.sc2.audit.thread;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import cn.hutool.core.io.watch.Watcher;

public class LogWatcher implements Watcher {

    

    // @Value("${log.path}")
    // private String logPath;


   

    @Override
    public void onCreate(WatchEvent<?> event, Path currentPath) {
    	System.out.println("onCreate....."+event.context().toString());
        // String fileName = event.context().toString();
//        File file = getSendFile(currentPath,fileName);
//        if (isNewDir(file)) {
//            log.info("Detected filename:{} is folder,continue processing children...",fileName);
//            log.info("dir path: {}",file.getAbsolutePath());
//            File[] files = file.listFiles();
//            log.info("list file size:{}",files.length);
//            for (File var : files) {
//                this.processExcptAndTransLogFile(var, var.getName());
//            }
//        }else {
//            this.processExcptAndTransLogFile(file, fileName);
//        }
    }

    @Override
    public void onModify(WatchEvent<?> event, Path currentPath) {
    	System.out.println("onModify...................."+event.context().toString());
    }

    @Override
    public void onDelete(WatchEvent<?> event, Path currentPath) {
    	System.out.println("onDelete>>>>>>>>>>>>>>>>>>>>>>"+event.context().toString());
//        if (isException(event.context().toString())) {
//            System.out.println("delete:"+event.context());
//        }
    }

    @Override
    public void onOverflow(WatchEvent<?> event, Path currentPath) {
    	System.out.println("+++++++++++++++++++++++");
//        if (isException(event.context().toString())) {
//            System.out.println(event.context());
//        }
    }

   
}
