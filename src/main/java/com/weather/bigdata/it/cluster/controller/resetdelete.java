package com.weather.bigdata.it.cluster.controller;

import com.alibaba.fastjson.JSON;
import com.weather.bigdata.it.cluster.db.localDB;
import com.weather.bigdata.it.cluster.verification;
import com.weather.bigdata.it.spark.sparksubmit.sparksubmit_PropertiesUtil;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSConfUtil;
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
public class resetdelete {

    private static Logger logger = LoggerFactory.getLogger(resetdelete.class);
    private static String delete_MsgKey="delete_Msg";

    @RequestMapping(value = "/reset/delete", method = RequestMethod.POST)
    public static Object verify(HttpServletRequest req, String user, String password, String regionKey,String dataType){
        Map<String, String> result =  ( Map<String, String>) verification.register(req,user,password);
        boolean isroot=verification.isRoot(result);

        if(isroot){
            result=resetdelete.delete(result,regionKey,dataType);
        }else{
            boolean isadmin0=verification.isAdmin0(result);
            boolean isadmin1=verification.isAdmin1(result);
            boolean isit=verification.isIt(result);
            boolean ismt=verification.isMt(result);
            if(localDB.isOnlineRegion(regionKey)){
                if(isadmin0){
                    result=resetdelete.delete(result,regionKey,dataType);
                }else{
                    result.put(resetdelete.delete_MsgKey, regionKey+"线上区域保护,"+dataType+",重置失败,请使用admin0进行相关操作");
                }
            }else if(localDB.isOnlineSecRegion(regionKey) || localDB.isPatchRegion(regionKey)){
                if(isadmin1){
                    result=resetdelete.delete(result,regionKey,dataType);
                }else{
                    result.put(resetdelete.delete_MsgKey, regionKey+"区域保护,"+dataType+",重置失败,请使用admin1进行相关操作");
                }
            }else if(localDB.isMtRegion(regionKey)){
                if(isadmin1 || ismt){
                    result=resetdelete.delete(result,regionKey,dataType);
                }else{
                    result.put(delete_MsgKey, "请使用mt相关账户操作该区域");
                }
            }else if(localDB.isItRegion(regionKey)){
                if (isadmin0 || isit){
                    result=resetdelete.delete(result,regionKey,dataType);
                }else{
                    result.put(delete_MsgKey, "请使用it相关账户操作该区域");
                }
            }else{
                result=resetdelete.delete(result,regionKey,dataType);
            }
        }

        logger.info("用户"+user+"请求重置区域"+regionKey);
        return returnUtil.showReturn(logger,result);
    }
    private static Map<String, String> delete(Map<String, String> result, String regionKey,String dataType){
        String upload_dir0 = localDB.getupload_dir();
        String upload_dir1= HDFSConfUtil.formatFilename(upload_dir0);
        String upload_dir=upload_dir1;
        if(dataType.equals("mt_commons")) {
            upload_dir = sparksubmit_PropertiesUtil.getmt_commonsLibPath_regionKey(regionKey,upload_dir1);
        }else if(dataType.equals("platform")){
            upload_dir = sparksubmit_PropertiesUtil.getplatformlibPath_regionKey(regionKey,upload_dir1);
        }else{
            upload_dir=sparksubmit_PropertiesUtil.getsignallibPath_dataType_regionKey(dataType,regionKey,upload_dir1);
        }
        boolean flag=HDFSOperation1.delete(upload_dir);
        if(flag){
            result.put("msg", regionKey+","+dataType+",重置成功");
        }else{
            result.put("msg", regionKey+","+dataType+",重置失败");
        }
        return result;
    }
}
