package com.pccw.sc2.audit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.service.AuditLogService;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Value("${ws.host}")
    private String restfulHost;//http://172.16.254.76:8080

    private Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private Logger trackLog = LoggerFactory.getLogger("track_log");
    private Logger excptLog = LoggerFactory.getLogger("excpt_log");
    private Logger transLog = LoggerFactory.getLogger("trans_log");

    private final String RESTFUL_EX_URL = "/smartics/srk/insertWkskExLog";
    private final String RESTFUL_TX_URL = "/smartics/srk/insertWkskTxLog";

    RestTemplate restTemplate = new RestTemplate();

    @Override
    @Async
    public Future<Integer> sendTranscation(List<TransationLogVO> requestEntity) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(this.buildTranscationUrl(), this.buildJsonObjectTrans(requestEntity), String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        return new AsyncResult<Integer>(statusCodeValue);
    }

    @Override
    @Async
    public Future<Integer> sendException(List<ExceptionLogVO> requestEntity) {
        this.log.info("start send exception .......");
        String url = this.buildExceptionUrl();
        Map<String,Object> entity = this.buildJsonObjectExcpt(requestEntity);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        return new AsyncResult<Integer>(statusCodeValue);
        // ThreadUtil.sleep(5000);

//        Future<String> future;
//        try {
//            Thread.sleep(1000 * 1);
//            future = new AsyncResult<String>("success:" + i);
//        } catch (InterruptedException e) {
//            future = new AsyncResult<String>("error");
//        }
//        return future;
    }

    @Override
    public void processExceptionLog(JSONObject payload) {

        // ExceptionLogVO exceptionLogVO = JSON.parseObject(payload.toString(), ExceptionLogVO.class);
        excptLog.info(payload.toString());

//        ExceptionLogVO excptVO = new ExceptionLogVO();
//        excptVO.setActiontype("AIB");
//        excptVO.setDtaction("2018-08-03T11:58:09");
//        excptVO.setDtlstupd("2018-08-04T11:58:09");
//        excptVO.setDtual("2018-08-04T11:58:09");
//        excptVO.setKskid("0001");
//        excptVO.setMcdno("03013001000037BE");
//        excptVO.setRemk("remark");
//        excptVO.setExcptcd("ERKI0038");
//        excptVO.setLstupid("KSK_AUD");
    }

    @Override
    public void processOperationLog(JSONObject payload) {

        // TransationLogVO transationLogVO = JSON.parseObject(payload.toString(), TransationLogVO.class);

//        transationLogVO.setActiontype("AIB");
//        transationLogVO.setDtaction("2018-08-02T11:30:30");
//        transationLogVO.setDtual("2018-08-02T11:30:30");
//        transationLogVO.setRemk("remark");
//        transationLogVO.setResult("S");
//        transationLogVO.setKskid("0001");
//        transationLogVO.setDtlstupd("2018-08-02T11:30:30");
//        transationLogVO.setLstupid("KSK_AUD");
//        transationLogVO.setMcdno("03013001000037BE");
        transLog.info(payload.toString());
    }

    @Override
    public void processTrackLog(JSONObject payload) {
        boolean hasMessage = payload.has("message");
        if (hasMessage) {
            String message = payload.getString("message");
            if (message != null && message.length() > 0) {
                trackLog.info(String.format("msg-->%s", message));
            }
            return;
        }
        JSONObject head = payload.getJSONObject("head");
        JSONObject resq = payload.getJSONObject("resq");
        JSONObject resp = payload.getJSONObject("resp");
        trackLog.info(String.format("request header:%s, payload:%s", head, resq));
        trackLog.info(String.format("=====>response:%s", resp));
    }

    private String buildTranscationUrl() {
        return this.restfulHost + RESTFUL_TX_URL;
    }

    private String buildExceptionUrl() {
        return this.restfulHost + RESTFUL_EX_URL;
    }

    private Map<String,Object> buildJsonObjectExcpt(List<ExceptionLogVO> requestEntity) {

//        List<ExceptionLogVO> voList = requestEntity.stream().map(x -> getLogVO(x,ExceptionLogVO.class)).collect(Collectors.toList());
        return new HashMap<String,Object>() {
            private static final long serialVersionUID = 1L;
			{
                put("lkskExLgList", requestEntity);
            }
        };
    }

    private Map<String,Object> buildJsonObjectTrans(List<TransationLogVO> requestEntity) {
//        List<TransationLogVO> voList = requestEntity.stream().map(x -> getLogVO(x,TransationLogVO.class)).collect(Collectors.toList());
        return new HashMap<String,Object>() {
            private static final long serialVersionUID = 1L;
			{
                put("lkskTxLgList", requestEntity);
            }
        };
    }
}
