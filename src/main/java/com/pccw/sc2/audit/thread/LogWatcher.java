package com.pccw.sc2.audit.thread;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.regex.Pattern;

public class LogWatcher implements Watcher {

    @Value("${log.path}")
    private String logPath;

    @Autowired
    @Qualifier("exceptionLineHandler")
    private AbstractLineHandler exceptionLineHandler;

    @Autowired
    @Qualifier("transcationLineHandler")
    private AbstractLineHandler transactionLineHandler;

    private Pattern excptPattern = Pattern.compile("excpt-\\d{8}-\\d+.log");
    private Pattern transPattern = Pattern.compile("trans-\\d{8}-\\d+.log");

    private boolean isException(String fileName) {
        return excptPattern.matcher(fileName).matches();
    }
    private boolean isTranscation(String fileName) {
        return transPattern.matcher(fileName).matches();
    }

    @Override
    public void onCreate(WatchEvent<?> event, Path currentPath) {
        String fileName = event.context().toString();
        File file = getSendFile(currentPath,fileName);
        if (isTranscation(fileName)) {
            FileUtil.readUtf8Lines(file, this.transactionLineHandler);
            this.transactionLineHandler.after();
        }else if (isException(fileName)) {
            FileUtil.readUtf8Lines(file, this.exceptionLineHandler);
            this.exceptionLineHandler.after();
        }
    }

    @Override
    public void onModify(WatchEvent<?> event, Path currentPath) {
    }

    @Override
    public void onDelete(WatchEvent<?> event, Path currentPath) {
//        if (isException(event.context().toString())) {
//            System.out.println("delete:"+event.context());
//        }
    }

    @Override
    public void onOverflow(WatchEvent<?> event, Path currentPath) {
        if (isException(event.context().toString())) {
            System.out.println(event.context());
        }
    }

    private File getSendFile(Path parentPath,String fileName) {
        return new File(parentPath.toString()+File.separator+fileName);
    }
}
