package com.pccw.sc2.audit.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.pccw.sc2.audit.service.AuditLogService;

public interface AbstractHandler<T> {

    Pattern pattern = Pattern.compile("\\{.*\\}");
    
    public static final String RESTFUL_EX_URL = "/smartics/srk/insertWkskExLog";
    public static final String RESTFUL_TX_URL = "/smartics/srk/insertWkskTxLog";

    public Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    public Logger resultLog = LoggerFactory.getLogger("result_log");

    default T getLogVO(String log, TypeReference<T> clazz) {
        Matcher matcher = pattern.matcher(log);
        if (matcher.find()) {
            return JSON.parseObject(matcher.group(0),clazz);
        }
        return null;
    }

    default List<T> transform(List<String> list) {
        List<T> voList = list.stream().map(x -> getLogVO(x, new TypeReference<T>(){}  )).collect(Collectors.toList());
        return voList;
    }

    default public void handle(List<String> list) {
        if (list != null && list.size()>0) {
            List<T> voList = transform(list);
            Future<Integer> future = getAuditLogService().send(buildRequestEntity(voList),this.getRestfulUrl());
            int i = 0;
            while (true) {
               if (i >= 60) {
                   log.warn("send {} exceed the limit 60 seconds.",getFileName());
                   resultLog.warn("overtime..{}",getFileName());
                   break;
               } else {
                   i++;
               }
                if (future.isCancelled()) {
                    log.warn("send {} has be canceled.",getFileName());
                    break;
                }
                if (future.isDone()) {
                    log.info("send {} is done.",getFileName());
                    try {
						Integer statusCode = future.get();
						log.info("send {} success.{}",getFileName(), statusCode.toString());
						resultLog.info("==>>success.{}",this.getFileName());
					} catch (Exception e) {
                        Throwable ee =  e.getCause() == null ? e : e.getCause();
                        resultLog.error("filename:{} ,error:{}",this.getFileName(),ee.getMessage());
                        log.error("call restful ws error.",e.getCause());
                    }
                    break;
                }
                ThreadUtil.sleep(1000);
            }
        }
    }

    public void setFileName(String fileName);

    public String getFileName();
    
    public String getRestfulUrl();

    public Map<String, Object> buildRequestEntity(List<T> requestEntity);

    public AuditLogService getAuditLogService();
}
