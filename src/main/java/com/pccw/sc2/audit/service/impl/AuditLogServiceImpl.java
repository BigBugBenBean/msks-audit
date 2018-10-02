package com.pccw.sc2.audit.service.impl;

import java.util.Map;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pccw.sc2.audit.service.AuditLogService;

@Service
public class AuditLogServiceImpl implements AuditLogService {

//	private Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);
	private Logger log = LogManager.getLogger(AuditLogServiceImpl.class);

	private Logger trackLog = LogManager.getLogger("track_log");
	private Logger excptLog = LogManager.getLogger("excpt_log");
	private Logger transLog = LogManager.getLogger("trans_log");
	
	RestTemplate restTemplate = new RestTemplate();

	@Override
	@Async
	public <T> Future<Integer> send(Map<String, Object> requestEntity,String restUrl) {
		this.log.info("start invoking restful webservice .......");
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(restUrl,requestEntity, String.class);
//		int statusCodeValue = responseEntity.getStatusCodeValue();
		return new AsyncResult<Integer>(responseEntity.getStatusCodeValue());
//		return new AsyncResult<Integer>(Integer.parseInt("444"));
	}

//	@Override
//	@Async
//	public Future<Integer> sendTranscation(List<TransationLogVO> requestEntity,String restUrl) {
//		this.log.info("start send transaction .......");
//		ResponseEntity<String> responseEntity = restTemplate.postForEntity(restUrl,
//				this.buildJsonObjectTrans(requestEntity), String.class);
////		int statusCodeValue = responseEntity.getStatusCodeValue();
//		return new AsyncResult<Integer>(requestEntity.size());
//	}

//	@Override
//	@Async
//	public Future<Integer> sendException(List<ExceptionLogVO> requestEntity,String restUrl) {
//		this.log.info("start send exception .......");
//		Map<String, Object> entity = this.buildJsonObjectExcpt(requestEntity);
//		ResponseEntity<String> responseEntity = restTemplate.postForEntity(restUrl, entity, String.class);
////		int statusCodeValue = responseEntity.getStatusCodeValue();
////		if (statusCodeValue == 200) {
////			this.log.info("sended size= {} ", requestEntity.size());
////		}
//		return new AsyncResult<Integer>(requestEntity.size());
//		// ThreadUtil.sleep(5000);
//
//		// Future<String> future;
//		// try {
//		// Thread.sleep(1000 * 1);
//		// future = new AsyncResult<String>("success:" + i);
//		// } catch (InterruptedException e) {
//		// future = new AsyncResult<String>("error");
//		// }
//		// return future;
//	}

	@Override
	public void processExceptionLog(JSONObject payload) {

		// ExceptionLogVO exceptionLogVO = JSON.parseObject(payload.toString(),
		// ExceptionLogVO.class);
		excptLog.info(payload.toString());

		// ExceptionLogVO excptVO = new ExceptionLogVO();
		// excptVO.setActiontype("AIB");
		// excptVO.setDtaction("2018-08-03T11:58:09");
		// excptVO.setDtlstupd("2018-08-04T11:58:09");
		// excptVO.setDtual("2018-08-04T11:58:09");
		// excptVO.setKskid("0001");
		// excptVO.setMcdno("03013001000037BE");
		// excptVO.setRemk("remark");
		// excptVO.setExcptcd("ERKI0038");
		// excptVO.setLstupid("KSK_AUD");
	}

	@Override
	public void processOperationLog(JSONObject payload) {

		// TransationLogVO transationLogVO = JSON.parseObject(payload.toString(),
		// TransationLogVO.class);

		// transationLogVO.setActiontype("AIB");
		// transationLogVO.setDtaction("2018-08-02T11:30:30");
		// transationLogVO.setDtual("2018-08-02T11:30:30");
		// transationLogVO.setRemk("remark");
		// transationLogVO.setResult("S");
		// transationLogVO.setKskid("0001");
		// transationLogVO.setDtlstupd("2018-08-02T11:30:30");
		// transationLogVO.setLstupid("KSK_AUD");
		// transationLogVO.setMcdno("03013001000037BE");
		transLog.info(payload.toString());
	}

	@Override
	public void processTrackLog(JSONObject payload) {
		boolean hasMessage = payload.has("message");
		boolean hasTimes = payload.has("times");
		String times = hasTimes ? payload.getString("times") :"";
		if (hasMessage) {
			String message = payload.getString("message");
			if (message != null && message.length() > 0) {
				trackLog.info(String.format("total:=>%sms ,msg-->%s",times, message));
			}
			return;
		}
		JSONObject head = payload.getJSONObject("head");
		JSONObject resq = payload.getJSONObject("resq");
		JSONObject resp = payload.getJSONObject("resp");
		resp = respFingerprint1(respAnsifptmp12inBase64(respPhoto(respPhotojp2inBase64(respMorphofptmp12inBase64(respPhotojpginBase64(respFingerprint0(fptmplinBase64(respPhotoBmp(fpdata(resp))))))))));
		resq = fptmplinBase64(reqFpimginBase64(resq));
		trackLog.info(String.format("=>reqt:%s, payload:%s", head, resq));
		trackLog.info(String.format("total:=>%sms ,resp<=:%s", times , resp));
	}

	private JSONObject respPhotoBmp(JSONObject payload) {
		String key = "photo_bmp";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...photo_bmp...");
		}
		return payload;
	}

	private JSONObject fpdata(JSONObject payload) {
		String key = "fpdata";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...fpdata...");
		}
		return payload;
	}

	private JSONObject fptmplinBase64(JSONObject payload) {
		String key = "fp_tmpl_in_base64";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...fp_tmpl_in_base64...");
		}
		return payload;
	}
	
	private JSONObject reqFpimginBase64(JSONObject payload) {
		String key = "fp_img_in_base64";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...fp_img_in_base64...");
		}
		return payload;
	}
	
	private JSONObject respPhotojpginBase64(JSONObject payload) {
		String key = "photo_jpg_in_base64";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...photo_jpg_in_base64...");
		}
		return payload;
	}
	
	private JSONObject respMorphofptmp12inBase64(JSONObject payload) {
		String key = "morpho_fp_tmp12_in_base64";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...morpho_fp_tmp12_in_base64...");
		}
		return payload;
	}
	
	private JSONObject respPhotojp2inBase64(JSONObject payload) {
		String key = "photo_jp2_in_base64";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...photo_jp2_in_base64...");
		}
		return payload;
	}
	
	private JSONObject respFingerprint0(JSONObject payload) {
		String key = "fingerprint0";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...fingerprint0...");
		}
		return payload;
	}

	private JSONObject respFingerprint1(JSONObject payload) {
		String key = "fingerprint1";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...fingerprint1...");
		}
		return payload;
	}
	
	private JSONObject respPhoto(JSONObject payload) {
		String key = "photo";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...photo...");
		}
		return payload;
	}
	
	private JSONObject respAnsifptmp12inBase64(JSONObject payload) {
		String key = "ansi_fp_tmp12_in_base64";
		boolean has = payload.has(key);
		if (has) {
			payload.put(key, "Omit...ansi_fp_tmp12_in_base64...");
		}
		return payload;
	}
}
