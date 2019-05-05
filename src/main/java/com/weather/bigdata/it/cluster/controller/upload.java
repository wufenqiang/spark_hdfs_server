package com.weather.bigdata.it.cluster.controller;

import com.weather.bigdata.it.cluster.db.localDB;
import com.weather.bigdata.it.cluster.verification;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSFile;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSOperation1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class upload {

	private static Logger logger = LoggerFactory.getLogger(upload.class);
    private static String upload_MsgKey="upload_Msg";

	@RequestMapping(value = "/deploy/upload", method = RequestMethod.POST)
    public static Object verify(HttpServletRequest req, String user,String password,String regionKey,String dataType,@RequestParam("file") MultipartFile file){
        Map<String, String> result= (Map<String, String>) verification.register(req,user,password);
        boolean isroot=verification.isRoot(result);
        if(isroot){
            result=upload.up(result,regionKey,dataType,file);
        }else{
            boolean isadmin0 = verification.isAdmin0(result);
            boolean isadmin1 = verification.isAdmin0(result);
            boolean isit=verification.isIt(result);
            boolean ismt=verification.isMt(result);
            if(localDB.isOnlineRegion(regionKey) || localDB.isOnlineSecRegion(regionKey)) {
                result.put(upload_MsgKey, "不可直接部署线上区域、准线上区域");
            }else if( localDB.isPatchRegion(regionKey)){
                if(isadmin1){
                    result=upload.up(result,regionKey,dataType,file);
                }else {
                    result.put(upload_MsgKey, "权限不够,请使用admin1部署该区域");
                }
            }else if(localDB.isMtRegion(regionKey)){
                if(isadmin1 || ismt){
                    result=upload.up(result,regionKey,dataType,file);
                }else {
                    result.put(upload_MsgKey, "请使用mt相关账户部署该区域");
                }
            }else if(localDB.isItRegion(regionKey)){
                if(isadmin0 || isit){
                    result=upload.up(result,regionKey,dataType,file);
                }else {
                    result.put(upload_MsgKey, "请使用it相关账户部署该区域");
                }
            }else {
                result=upload.up(result,regionKey,dataType,file);
            }
        }
        logger.info("用户"+user+"请求上传部署");
        return returnUtil.showReturn(logger,result);
    }
    private static Map<String, String> up(Map<String, String> result,String regionKey,String dataType,@RequestParam("file") MultipartFile file) {
        String upload_dir= localDB.getlibPath(regionKey,dataType);
        if(!HDFSOperation1.exists(upload_dir)){
            HDFSOperation1.mkdirs(upload_dir);
        }
        String uploadFileName="";
        try {
            uploadFileName = java.net.URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Long start=System.currentTimeMillis();
            String uploadFileName_ab = upload_dir+"/"+uploadFileName;
            //file.transferTo(destFile);
            boolean writeFlag=HDFSFile.fileWriter(file.getInputStream(),uploadFileName_ab,true);
            Long end=System.currentTimeMillis();

            if(writeFlag){
                String msg="文件【" + uploadFileName + "】上传成功。";
                result.put("status", "success");
                result.put("msg", msg);
                result.put("fileName", uploadFileName);
                logger.info(msg+";文件写入时间:"+(end-start)/1000+"s,写入地址:"+uploadFileName_ab);
            }else{
                result.put("status", "error");
                result.put("msg", "文件【" + uploadFileName + "】写入失败,写入地址:"+uploadFileName_ab);
            }

        } catch (IllegalStateException | IOException e1) {
            result.put("status", "error");
            result.put("msg", "文件【" + uploadFileName + "】上传失败。" + e1.getMessage());
            e1.printStackTrace();
        }
        return result;
	}
}
