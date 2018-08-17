package com.pccw.sc2.audit.thread;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.service.AuditLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.thread.ThreadUtil;

public class TransactionLineHandler implements AbstractLineHandler<TransationLogVO> {

    private Logger log = LoggerFactory.getLogger(TransactionLineHandler.class);
    private Logger restLog = LoggerFactory.getLogger("rest_log");

    @Value("${trans.capacity}")
    private int capacity;

    private Set<String> set = new ConcurrentHashSet<>(capacity);

    @Autowired
    private AuditLogService auditLogService;

    private String fileName;

    @Override
    public void handle(String line) {
        if (this.set.size() < capacity) {
            this.set.add(line);
        } else if (this.set.size() >= capacity) {
            auditLogService.sendTranscation(change(set));
            set.clear();
        }
    }

    @Override
    public void after() {

        if (this.set.size() >0) {
            List<TransationLogVO> voList = change(this.set);
            Future<Integer> future = auditLogService.sendTranscation(voList);
            set.clear();
            int i = 0;
            while (true) {
                if (i >= 60) {
                    this.log.warn("send transaction exceed the limit 60 seconds.");
                    break;
                } else {
                    i++;
                }
                if (future.isCancelled()) {
                    this.log.warn("send transaction has be canceled.");
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
