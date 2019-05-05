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
public class uploadParameter {

	private static Logger logger = LoggerFactory.getLogger(uploadParameter.class);
    private static String uploadParameter_MsgKey="uploadParameter_Msg";

	@RequestMapping(value = "/deploy/reparameter", method = RequestMethod.POST)
    public static Object verify(HttpServletRequest req, String user,String password,String relativePath,@RequestParam("file") MultipartFile file){
        Map<String, String> result= (Map<String, String>) verification.register(req,user,password);
        boolean isroot=verification.isRoot(result);
        if(isroot){
            result= uploadParameter.up(result,relativePath,file);
        }else{
            boolean isadmin0 = verification.isAdmin0(result);
            boolean isadmin1 = verification.isAdmin0(result);
            boolean isit=verification.isIt(result);
            boolean ismt=verification.isMt(result);
            if(isadmin0 || isadmin1 || ismt || isit){
                result=uploadParameter.up(result,relativePath,file);
            }else{
                result.put(uploadParameter_MsgKey, "权限不够,请联系维护人员");
            }
        }
        logger.info("用户"+user+"请求上传参数文件");
        return returnUtil.showReturn(logger,result);
    }
    private static Map<String, String> up(Map<String, String> result,String RelativePath,@RequestParam("file") MultipartFile file) {
        String upload_dir= localDB.getParameterPath(RelativePath);
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
