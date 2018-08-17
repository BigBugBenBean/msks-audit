package com.pccw.sc2.audit.service;

import java.util.List;
import java.util.concurrent.Future;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;

import org.json.JSONObject;

public interface AuditLogService {
	
	void processExceptionLog(JSONObject payload);
	
	void processOperationLog(JSONObject payload);

	void processTrackLog(JSONObject payload);

	public Future<Integer> sendTranscation(List<TransationLogVO> set);

	public Future<Integer> sendException(List<ExceptionLogVO> set);

}
