package com.pccw.sc2.audit.thread;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.regex.Pattern;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.Watcher;

public class LogWatcher implements Watcher {

    private Logger log = LoggerFactory.getLogger(LogWatcher.class);

    @Value("${log.path}")
    private String logPath;

    @Autowired
    @Qualifier("exceptionLineHandler")
    private AbstractLineHandler<ExceptionLogVO> exceptionLineHandler;

    @Autowired
    @Qualifier("transcationLineHandler")
    private AbstractLineHandler<TransationLogVO> transactionLineHandler;

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
        log.info("on_create........."+fileName);
        File file = getSendFile(currentPath,fileName);
        if (isTranscation(fileName)) {
            this.transactionLineHandler.setFileName(fileName);
            FileUtil.readUtf8Lines(file, this.transactionLineHandler);
            this.transactionLineHandler.after();
        }else if (isException(fileName)) {
            this.exceptionLineHandler.setFileName(fileName);
            FileUtil.readUtf8Lines(file, this.exceptionLineHandler);
            this.exceptionLineHandler.after();
            log.info("send over...");
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
