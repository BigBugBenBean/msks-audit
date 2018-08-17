package com.pccw.sc2.audit.thread;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.service.AuditLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.thread.ThreadUtil;

public class ExceptionLineHandler implements AbstractLineHandler<ExceptionLogVO> {

    private Logger log = LoggerFactory.getLogger(ExceptionLineHandler.class);
    private Logger restLog = LoggerFactory.getLogger("rest_log");

    @Value("${excpt.capacity}")
    private int capacity;

    private Set<String> set = new ConcurrentHashSet<>(capacity);

    private String fileName;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void handle(String line) {
        if (this.set.size() < capacity) {
            this.set.add(line);
        } else if (this.set.size() >= capacity) {
            auditLogService.sendException(change(set));
            set.clear();
        }
    }

    @Override
    public void after() {
        if (this.set.size()>0) {
            List<ExceptionLogVO> change = change(this.set);
            Future<Integer> future = auditLogService.sendException(change);
            set.clear();
            int i = 0;
            while (true) {
                if (i >= 60) {
                    this.log.warn("send exception exceed the limit 60 seconds.");
                    break;
                } else {
                    i++;
                }
                if (future.isCancelled()) {
                    this.log.warn("send exception has be canceled.");
                    break;
                }
                if (future.isDone()) {
                    log.info("send exception is done.");
                    try {
						Integer body = future.get();
					} catch (Exception e) {
                        Throwable ee =  e.getCause() == null ? e : e.getCause();
                        this.restLog.error("filename:${} ,error:${}",this.fileName,ee.getMessage());
                        this.log.error("call restful ws error.",e);
                        // e.getCause().printStackTrace();
						// e.printStackTrace();
                    }
                    break;
                }
                ThreadUtil.sleep(1000);
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
