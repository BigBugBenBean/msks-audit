package com.pccw.sc2.audit.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.service.AuditLogService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Value("${ws.host}")
    private String restfulHost;//http://172.16.254.76:8080

    private static Logger trackLog = LoggerFactory.getLogger("track_log");
    private static Logger excptLog = LoggerFactory.getLogger("excpt_log");
    private static Logger transLog = LoggerFactory.getLogger("trans_log");

    private final String RESTFUL_EX_URL = "/smartics/srk/insertWkskExLog";
    private final String RESTFUL_TX_URL = "/smartics/srk/insertWkskTxLog";

    RestTemplate restTemplate = new RestTemplate();

    @Override
    @Async
    public void sendTranscation(List<TransationLogVO> requestEntity) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(this.buildTranscationUrl(), this.buildJsonObjectTrans(requestEntity), String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        String body = responseEntity.getBody();

        System.out.println("response status=" + statusCodeValue);
        System.out.println(body);
    }

    @Override
    @Async
    public void sendException(List<ExceptionLogVO> requestEntity) {
        String url = this.buildExceptionUrl();
        Map<String,Object> entity = this.buildJsonObjectExcpt(requestEntity);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        String body = responseEntity.getBody();

        System.out.println("response status=" + statusCodeValue);
        System.out.println(body);

        ThreadUtil.sleep(5000);

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

        ExceptionLogVO exceptionLogVO = JSON.parseObject(payload.toString(), ExceptionLogVO.class);

//        ExceptionLogVO exceptionLogVO = new ExceptionLogVO();

//        if(payload.has("actiontype")) {
//            exceptionLogVO.setActiontype(payload.getString("actiontype"));
//        }
//        if(payload.has("dtaction")) {
//            exceptionLogVO.setDtaction(payload.getString("dtaction"));
//        }
//        if(payload.has("dtlstupd")) {
//            exceptionLogVO.setDtlstupd(payload.getString("dtlstupd"));
//        }
//        if(payload.has("kskid")) {
//            exceptionLogVO.setKskid(payload.getString("kskid"));
//        }
//        if(payload.has("excptcd")) {
//            exceptionLogVO.setExcptcd(payload.getString("excptcd"));
//        }
//        if(payload.has("lstupid")) {
//            exceptionLogVO.setLstupid(payload.getString("lstupid"));
//        }
//        if(payload.has("mcdno")) {
//            exceptionLogVO.setMcdno(payload.getString("mcdno"));
//        }
//        if(payload.has("remk")) {
//            exceptionLogVO.setRemk(payload.getString("remk"));
//        }
//        if(payload.has("dtual")) {
//            exceptionLogVO.setDtual(payload.getString("dtual"));
//        }
        excptLog.info(exceptionLogVO.toString());
//        AuditLogThread<ExceptionLogVO> auditThread = new AuditLogThread(restTemplate, this.restfulHost+RESTFUL_EX_URL, buildJsonObjectExcpt(exceptionLogVO));
//        ThreadUtil.execute(auditThread);

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
//        TransationLogVO transationLogVO = new TransationLogVO();

        TransationLogVO transationLogVO = JSON.parseObject(payload.toString(), TransationLogVO.class);

//        if(payload.has("actiontype")) {
//            transationLogVO.setActiontype(payload.getString("actiontype"));
////            System.out.println(transationLogVO.getActiontype());
//        }
//        if(payload.has("dtaction")) {
//            transationLogVO.setDtaction(payload.getString("dtaction"));
////            System.out.println(transationLogVO.getDtaction());
//        }
//        if(payload.has("dtlstupd")) {
//            transationLogVO.setDtlstupd(payload.getString("dtlstupd"));
////            System.out.println(transationLogVO.getDtlstupd());
//        }
//        if(payload.has("kskid")) {
//            transationLogVO.setKskid(payload.getString("kskid"));
////            System.out.println(transationLogVO.getKskid());
//        }
//        if(payload.has("result")) {
//            transationLogVO.setResult(payload.getString("result"));
////            System.out.println(transationLogVO.getResult());
//        }
//        if(payload.has("lstupid")) {
//            transationLogVO.setLstupid(payload.getString("lstupid"));
////            System.out.println(transationLogVO.getLstupid());
//        }
//        if(payload.has("mcdno")) {
//            transationLogVO.setMcdno(payload.getString("mcdno"));
////            System.out.println(transationLogVO.getMcdno());
//        }
//        if(payload.has("remk")) {
//            transationLogVO.setRemk(payload.getString("remk"));
////            System.out.println(transationLogVO.getRemk());
//        }
//        if(payload.has("dtual")) {
//            transationLogVO.setDtual(payload.getString("dtual"));
////            System.out.println(transationLogVO.getDtual());
//        }

//        transationLogVO.setActiontype("AIB");
//        transationLogVO.setDtaction("2018-08-02T11:30:30");
//        transationLogVO.setDtual("2018-08-02T11:30:30");
//        transationLogVO.setRemk("remark");
//        transationLogVO.setResult("S");
//        transationLogVO.setKskid("0001");
//        transationLogVO.setDtlstupd("2018-08-02T11:30:30");
//        transationLogVO.setLstupid("KSK_AUD");
//        transationLogVO.setMcdno("03013001000037BE");
        transLog.info(transationLogVO.toString());
//        AuditLogThread<TransationLogVO> auditThread = new AuditLogThread(restTemplate, this.restfulHost+RESTFUL_TX_URL, buildJsonObjectTrans(transationLogVO));
//        ThreadUtil.execute(auditThread);
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
