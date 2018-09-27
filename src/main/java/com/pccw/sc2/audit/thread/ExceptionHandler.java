package com.pccw.sc2.audit.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.service.AuditLogService;

import cn.hutool.core.thread.ThreadUtil;

public class ExceptionHandler implements AbstractHandler<ExceptionLogVO> {

    private Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    private Logger resultLog = LoggerFactory.getLogger("result_log");

    @Value("${excpt.capacity}")
    private int capacity;
    
    @Value("${restful.host}")
	private String restfulHost; // http://172.16.254.76:8080

    private String fileName;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void handle(List<String> list) {

        if (list != null && list.size()>0) {
            List<ExceptionLogVO> voList = transform(list);
            Future<Integer> future = auditLogService.send(buildJsonObjectExcpt(voList),this.getRestfulUrl());
            int i = 0;
            while (true) {
               if (i >= 60) {
                   this.log.warn("send exception exceed the limit 60 seconds.");
                   this.resultLog.warn("overtime..{}",this.fileName);
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
						Integer statusCode = future.get();
						log.info("send exception success."+ statusCode.toString());
						this.resultLog.info("==>>success.{}",this.fileName);
					} catch (Exception e) {
                        Throwable ee =  e.getCause() == null ? e : e.getCause();
                        this.resultLog.error("filename:{} ,error:{}",this.fileName,ee.getMessage());
                        this.log.error("call restful ws error.",e.getCause());
                    }
                    break;
                }
                ThreadUtil.sleep(1000);
            }
        }
    }
    

	public String getRestfulUrl() {
		return this.restfulHost + AbstractHandler.RESTFUL_EX_URL;
	}

    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    private Map<String, Object> buildJsonObjectExcpt(List<ExceptionLogVO> requestEntity) {
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("lkskExLgList", requestEntity);
			}
		};
    }

}
