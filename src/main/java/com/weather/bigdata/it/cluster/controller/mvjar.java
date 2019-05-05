package com.weather.bigdata.it.cluster.controller;

import com.weather.bigdata.it.cluster.db.localDB;
import com.weather.bigdata.it.cluster.verification;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSOperation1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class mvjar {
    private static Logger logger = LoggerFactory.getLogger(mvjar.class);
    private static String mvjar_MsgKey="mvjar_Msg";
    @RequestMapping(value = "/operation/mvjar", method = RequestMethod.POST)
    public static Object verify(HttpServletRequest req, String user, String password,String region_from,String region_to,String dataType){
        Map<String, String> result= (Map<String, String>) verification.register(req,user,password);
        boolean isroot=verification.isRoot(result);

        if(isroot){
            result = mvjar.mv(result, region_from, region_to, dataType);
        }else {
            boolean isadmin0 = verification.isAdmin0(result);
            boolean isadmin1 = verification.isAdmin0(result);
            boolean isit=verification.isIt(result);
            boolean ismt=verification.isMt(result);
            if(localDB.isOnlineRegion(region_to)) {
                if(localDB.isOnlineSecRegion(region_from)){
                    if (isadmin0) {
                        result = mvjar.mv(result, region_from, region_to, dataType);
                    } else {
                        result.put(mvjar_MsgKey, "权限不够,不可移动到线上区域");
                    }
                }else{
                    result.put(mvjar_MsgKey, "只允许将测试后的准线上区域移动到线上区域");
                }
            }else if(localDB.isOnlineSecRegion(region_to)){
                if (isadmin1) {
                    result = mvjar.mv(result, region_from, region_to, dataType);
                } else {
                    result.put(mvjar_MsgKey, "权限不够,不可移动到准线上区域,使用admin1操作该区域");
                }
            }else if(localDB.isPatchRegion(region_to) ) {
                if (isadmin1) {
                    result = mvjar.mv(result, region_from, region_to, dataType);
                } else {
                    result.put(mvjar_MsgKey, "权限不够,不可移动到补丁区域,请使用admin1操作该区域");
                }
            }else if(localDB.isMtRegion(region_to)){
                if (isadmin1 || ismt) {
                    result = mvjar.mv(result, region_from, region_to, dataType);
                } else {
                    result.put(mvjar_MsgKey, "请使用mt相关账户操作该区域");
                }
            }else if(localDB.isItRegion(region_to)){
                if(isadmin0 || isit){
                    result=mvjar.mv(result,region_from,region_to,dataType);
                }else{
                    result.put(mvjar_MsgKey, "请使用it相关账户操作该区域");
                }
            }else{
                result=mvjar.mv(result,region_from,region_to,dataType);
            }
        }

        logger.info("用户"+user+"请求移动部署区域dataType("+dataType+"):"+"region_from("+region_from+")->region_to("+region_to+")");
        return returnUtil.showReturn(logger,result);
    }

    private static Map<String, String> mv(Map<String, String> result,String region_from,String region_to,String dataType){

        String labPath_from= localDB.getlibPath(region_from,dataType);
        String labPath_to=localDB.getlibPath(region_to,dataType);
        logger.info("labPath_from("+labPath_from+")->labPath_to("+labPath_to+")");
        boolean flag=HDFSOperation1.copyfile(labPath_from,labPath_to,true,false);
        String mvjarResult=region_from+"->"+region_to+",区域复制结果"+flag;
        result.put("mvjar",mvjarResult);

        logger.info("用户请求复制测试区");
        return result;
    }
}
