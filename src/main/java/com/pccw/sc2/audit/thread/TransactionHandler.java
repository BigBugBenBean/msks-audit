package com.pccw.sc2.audit.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.service.AuditLogService;

import cn.hutool.core.thread.ThreadUtil;

public class TransactionHandler implements AbstractHandler<TransationLogVO> {

    @Value("${trans.capacity}")
    private int capacity;

    @Value("${restful.host}")
	private String restfulHost; // http://172.16.254.76:8080

    @Autowired
    private AuditLogService auditLogService;

    private String fileName;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public AuditLogService getAuditLogService() {
        return this.auditLogService;
    }

    public String getRestfulUrl() {
		return this.restfulHost + AbstractHandler.RESTFUL_TX_URL;
	}
    
    public Map<String, Object> buildRequestEntity(List<TransationLogVO> requestEntity) {
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("lkskTxLgList", requestEntity);
			}
		};
	}
}
