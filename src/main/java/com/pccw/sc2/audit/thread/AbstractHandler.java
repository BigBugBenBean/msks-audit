package com.pccw.sc2.audit.thread;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public interface AbstractHandler<T> {

    Pattern pattern = Pattern.compile("\\{.*\\}");
    
    public static final String RESTFUL_EX_URL = "/smartics/srk/insertWkskExLog";
    public static final String RESTFUL_TX_URL = "/smartics/srk/insertWkskTxLog";

    default T getLogVO(String log, TypeReference<T> clazz) {
        Matcher matcher = pattern.matcher(log);
        if (matcher.find()) {
            return JSON.parseObject(matcher.group(0),clazz);
        }
        return null;
    }

    default List<T> transform(List<String> set) {
        List<T> voList = set.stream().map(x -> getLogVO(x, new TypeReference<T>(){}  )).collect(Collectors.toList());
        return voList;
    }

    public void setFileName(String fileName);
    
    public void handle(List<String> list);
    
    public String getRestfulUrl();
}
