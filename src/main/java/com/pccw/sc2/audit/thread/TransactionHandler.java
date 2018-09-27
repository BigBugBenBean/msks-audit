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

    private Logger log = LoggerFactory.getLogger(TransactionHandler.class);
    private Logger resultLog = LoggerFactory.getLogger("result_log");

    @Value("${trans.capacity}")
    private int capacity;

    @Value("${restful.host}")
	private String restfulHost; // http://172.16.254.76:8080

    @Autowired
    private AuditLogService auditLogService;

    private String fileName;

    @Override
    public void handle(List<String> list) {

    	if (list != null && list.size()>0) {
            List<TransationLogVO> voList = transform(list);
            Future<Integer> future = auditLogService.send(buildJsonObjectTrans(voList),this.getRestfulUrl());
            int i = 0;
            while (true) {
                if (i >= 60) {
                    this.log.warn("send transaction exceed the limit 60 seconds.");
                    this.resultLog.warn("overtime..{}",this.fileName);
                    break;
                } else {
                    i++;
                }
                if (future.isCancelled()) {
                    this.log.warn("send transaction has be canceled.");
                    break;
                }
                if (future.isDone()) {
                    log.info("send transaction is done.");
                    try {
                        Integer statusCode = future.get();
                        log.info("send transaction success."+ statusCode.toString());
                        this.resultLog.info("==>>success.{}",this.fileName);
					} catch (Exception e) {
                        Throwable ee =  e.getCause() == null ? e : e.getCause();
                        this.resultLog.error("filename:{} ,error:{}",this.fileName,ee.getMessage());
                        this.log.error("call restful ws error.",e);
                    }
                    break;
                }
                ThreadUtil.sleep(1000);
            }
        }
    }
    
    public String getRestfulUrl() {
		return this.restfulHost + AbstractHandler.RESTFUL_TX_URL;
	}

    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    private Map<String, Object> buildJsonObjectTrans(List<TransationLogVO> requestEntity) {
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("lkskTxLgList", requestEntity);
			}
		};
	}
}
