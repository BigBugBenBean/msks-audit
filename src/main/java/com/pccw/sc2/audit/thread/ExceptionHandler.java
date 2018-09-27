package com.pccw.sc2.audit.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.service.AuditLogService;

public class ExceptionHandler implements AbstractHandler<ExceptionLogVO> {

    @Value("${excpt.capacity}")
    private int capacity;
    
    @Value("${restful.host}")
	private String restfulHost; // http://172.16.254.76:8080

    private String fileName;

    @Autowired
    private AuditLogService auditLogService;

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

    @Override
	public String getRestfulUrl() {
		return this.restfulHost + AbstractHandler.RESTFUL_EX_URL;
	}
    
    public Map<String, Object> buildRequestEntity(List<ExceptionLogVO> requestEntity) {
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("lkskExLgList", requestEntity);
			}
		};
    }
}
