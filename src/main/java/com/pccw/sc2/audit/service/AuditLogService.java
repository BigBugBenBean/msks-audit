package com.pccw.sc2.audit.service;

import java.util.Map;
import java.util.concurrent.Future;

import org.json.JSONObject;

public interface AuditLogService {

	void processExceptionLog(JSONObject payload);

	void processOperationLog(JSONObject payload);

	void processTrackLog(JSONObject payload);

//	public Future<Integer> sendTranscation(List<TransationLogVO> set, String restUrl);

//	public Future<Integer> sendException(List<ExceptionLogVO> set, String restUrl);

	public <T> Future<Integer> send(Map<String, Object> list, String restUrl);

}
