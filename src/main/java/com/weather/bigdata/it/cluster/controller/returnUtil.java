package com.weather.bigdata.it.cluster.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Map;

public class returnUtil {
    public static Object showReturn(Logger logger,Map<String, String> result){

        //if (!StringUtils.isEmpty(callback)) {
        //    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
        //    mappingJacksonValue.setValue(callback);
        //    return mappingJacksonValue;
        //} else {
        //    return result;
        //}
        long timeStamp=System.currentTimeMillis();
        result.put("服务返回时间戳",String.valueOf(timeStamp));
        result.put("服务返回时间",String.valueOf(new Date(timeStamp)));
        logger.info("服务器返回结果为：{}。", JSON.toJSONString(result));
        return result;
    }
}
