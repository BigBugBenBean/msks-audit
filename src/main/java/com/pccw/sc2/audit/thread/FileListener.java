package com.pccw.sc2.audit.thread;

import java.util.List;
import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;

import cn.hutool.core.io.FileUtil;

public class FileListener extends FileAlterationListenerAdaptor {
    private Logger log = LogManager.getLogger(FileListener.class);
    
    @Autowired
    @Qualifier("exceptionLineHandler")
    private AbstractHandler<ExceptionLogVO> exceptionLineHandler;

    @Autowired
    @Qualifier("transcationLineHandler")
    private AbstractHandler<TransationLogVO> transactionLineHandler;

    private Pattern excptPattern = Pattern.compile("excpt-\\d{8}-\\d+.log");
    private Pattern transPattern = Pattern.compile("trans-\\d{8}-\\d+.log");

    private boolean isException(String fileName) {
        return excptPattern.matcher(fileName).matches();
    }
    
    private boolean isTranscation(String fileName) {
        return transPattern.matcher(fileName).matches();
    }
    
    private void processExcptAndTransLogFile(File file,String fileName) {
        log.info("start process {}...",fileName);
        if (isTranscation(fileName)) {
            this.transactionLineHandler.setFileName(fileName);
            List<String> list = FileUtil.readUtf8Lines(file);
            this.transactionLineHandler.handle(list);
        }else if (isException(fileName)) {
            this.exceptionLineHandler.setFileName(fileName);
            List<String> list = FileUtil.readUtf8Lines(file);
            this.exceptionLineHandler.handle(list);
        }else {
        	log.info("the file is neither excpt nor trans,ignore.");
        }
    }
    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
    		processExcptAndTransLogFile(file,file.getName());
//        log.info("[新建]:" + file.getAbsolutePath());
    }

    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {
//        log.info("[修改文件]:" + file.getAbsolutePath());
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
//        log.info("[删除文件]:" + file.getAbsolutePath());
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
//        log.info("[新建目录]:" + directory.getAbsolutePath());
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
//        log.info("[修改目录]:" + directory.getAbsolutePath());
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
//        log.info("[删除目录]:" + directory.getAbsolutePath());
    }

    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
    
//    private File getSendFile(Path parentPath,String fileName) {
//        return new File(parentPath.toString()+File.separator+fileName);
//    }
}
