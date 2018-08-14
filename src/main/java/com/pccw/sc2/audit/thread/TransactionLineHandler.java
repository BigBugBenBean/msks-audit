package com.pccw.sc2.audit.thread;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;

public class TransactionLineHandler implements AbstractLineHandler<TransationLogVO> {

    @Value("${trans.capacity}")
    private int capacity;

    private Set<String> set = new ConcurrentHashSet<>(capacity);

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void handle(String line) {
        //todo last handle
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
            auditLogService.sendTranscation(voList);
            set.clear();
        }
    }

}
