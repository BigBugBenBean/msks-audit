package com.pccw.sc2.audit.service;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;

public interface AuditLogService {
	
	void processExceptionLog(JSONObject payload);
	
	void processOperationLog(JSONObject payload);

	void processTrackLog(JSONObject payload);

	public void sendTranscation(List<TransationLogVO> set);

	public void sendException(List<ExceptionLogVO> set);

}
