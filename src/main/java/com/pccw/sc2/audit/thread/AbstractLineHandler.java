package com.pccw.sc2.audit.thread;

import cn.hutool.core.io.LineHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface AbstractLineHandler<T> extends LineHandler {

    public void after();

    Pattern pattern = Pattern.compile("\\{.*\\}");

    default T getLogVO(String log, TypeReference<T> clazz) {
        Matcher matcher = pattern.matcher(log);
        if (matcher.find()) {
            return JSON.parseObject(matcher.group(0),clazz);
        }
        return null;
    }

    default List<T> change(Set<String> set) {
        List<T> voList = set.stream().map(x -> getLogVO(x, new TypeReference<T>(){}  )).collect(Collectors.toList());
        return voList;
    }
}
