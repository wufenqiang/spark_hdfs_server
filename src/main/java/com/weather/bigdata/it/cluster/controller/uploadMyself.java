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
public class uploadMyself {

	private static Logger logger = LoggerFactory.getLogger(uploadMyself.class);
    private static String uploadMyself_MsgKey="uploadMyself_Msg";

	@RequestMapping(value = "/deploy/uploadMyself", method = RequestMethod.POST)
    public static Object verify(HttpServletRequest req, String user,String password,String exampleKey,@RequestParam("file") MultipartFile file){
        Map<String, String> result= (Map<String, String>) verification.register(req,user,password);
        boolean isroot=verification.isRoot(result);
        if(isroot){
            String msg="user("+user+")申请更新本接口服务";
            logger.info(msg);
            result.put("status", "success");
            result.put(uploadMyself_MsgKey, msg);
            result=uploadMyself.up(result,exampleKey,file);
            returnUtil.showReturn(logger,result);

        }else{
            result.put("status", "refuse");
            result.put(uploadMyself_MsgKey, "权限不够,请联系维护人员");
        }
        logger.info("用户"+user+"请求更新接口");
        return returnUtil.showReturn(logger,result);
    }

    private static Map<String, String> up( Map<String, String> result,String exampleKey,@RequestParam("file") MultipartFile file) {
        String spark_hdfs_server_rootPath=localDB.getspark_hdfs_server_Path(exampleKey);

        if(!HDFSOperation1.exists(spark_hdfs_server_rootPath)){
            HDFSOperation1.mkdirs(spark_hdfs_server_rootPath);
        }
        String uploadFileName="";
        try {
            uploadFileName = java.net.URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Long start=System.currentTimeMillis();
            String uploadFileName_ab = spark_hdfs_server_rootPath+"/"+uploadFileName;
            //file.transferTo(destFile);
            boolean writeFlag=HDFSFile.fileWriter(file.getInputStream(),uploadFileName_ab,true);
            Long end=System.currentTimeMillis();

            if(writeFlag){
                String msg="文件【" + uploadFileName + "】上传成功。";
                result.put("status", "success");
                result.put("msg", msg);
                result.put("exampleKey",exampleKey);
                result.put("fileName", uploadFileName);
                logger.info(msg+";文件写入时间:"+(end-start)/1000+"s,写入地址:"+uploadFileName_ab);

                result=exit(result);

            }else{
                result.put("status", "error");
                result.put("msg", "文件【" + uploadFileName + "】写入失败,写入地址:"+uploadFileName_ab);
            }

        } catch (IllegalStateException | IOException e1) {
            result.put("status", "error");
            result.put("msg", "文件【" + uploadFileName + "】上传失败。" + e1.getMessage());
            e1.printStackTrace();
        }
		logger.info("用户请求更新服务接口。");
        return result;
	}

    private static Map<String, String> exit(Map<String, String> result){
        System.exit(0);
        return result;
    }
    //public Object verification(HttpServletRequest req,String user,String password,String regionKey,String dataType,String callback){
    //
    //
    //    Properties AccountRegistration=localDB.getAccountRegistration();
    //    if(AccountRegistration.containsKey(user)){
    //        String password0=localDB.password(AccountRegistration,user);
    //        if(password0.equals(password)){
    //            //System.out.println(password);
    //
    //
    //            if(localDB.getprotectedRegionSet().contains(regionKey)){
    //                Map<String, String> result = new HashMap<String, String>();
    //                result.put("status", "prohibited");
    //                result.put("msg", "文件上传拒绝。该区域被保护,请选择其他区域上传测试依赖");
    //                return result;
    //            }else{
    //                if (req.getCharacterEncoding() == null) {
    //                    try {
    //                        req.setCharacterEncoding("UTF-8");
    //                    } catch (UnsupportedEncodingException e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //                return this.upload(file,upload_dir,regionKey,dataType,callback);
    //            }
    //        }else{
    //            Map<String, String> result = new HashMap<String, String>();
    //            result.put("status", "refuse");
    //            result.put("msg", "拒绝。密码错误");
    //            return result;
    //        }
    //    }else{
    //        Map<String, String> result = new HashMap<String, String>();
    //        result.put("status", "refuse");
    //        result.put("msg", "拒绝。该用户未注册");
    //        return result;
    //    }
    //}
    //@RequestMapping(value = "/file/upload1", method = RequestMethod.POST)
    //public static void uploadFile(HttpServletRequest req,Object file0) {
    //
	 //   File file1=(File)file0;
    //
	 //   FileBody file=new FileBody(file1);
    //
    //    System.out.println(req.getRemoteHost());
    //    System.out.println(req.getRemotePort());
    //    String url = "http://localhost:8082/file/upload";
    //    //url = "http://"+req.getRemoteHost()+":"+req.getRemotePort()+"/file/upload";
    //    //httpPostUploadInputStream(url,file);
    //
    //
    //    String disDir = "F:/tmp/upload/";//上传到服务器的文件目录
    //
    //    CloseableHttpClient httpClient = null;
    //    CloseableHttpResponse response = null;
    //    try {
    //        //httpClient = HttpClients.createDefault();
    //        httpClient = HttpClients.createSystem();
    //
    //
    //        // 把一个普通参数和文件上传给下面这个地址 是一个servlet
    //        //HttpPost httpPost = new HttpPost(url);
    //        HttpPost httpPost = new HttpPost(req.getRequestURL().toString());
    //
    //
    //
    //        //// 把文件转换成流对象FileBody
    //        //File localFile = new File(file);
    //        //System.out.println("本地文件大小："+localFile.length());
    //        //FileBody bin = new FileBody(localFile);
    //
    //        // StringBody userName = new StringBody("Scott",
    //        // ContentType.create("text/plain", Consts.UTF_8));
    //        // StringBody password = new StringBody("123456",
    //        // ContentType.create("text/plain", Consts.UTF_8));
    //        StringBody disDirBody = new StringBody(disDir, ContentType.create("text/plain", Consts.UTF_8));
    //
    //        HttpEntity httpEntity = MultipartEntityBuilder.create()
    //                .setCharset(Charset.forName("utf-8"))
    //                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
    //                // 相当于<input type="file" name="file"/>
    //                .addPart("file", file)
    //
    //                // 相当于<input type="text" name="userName" value=userName>
    //                // .addPart("userName", userName).addPart("pass",
    //                // password).build();
    //                .addPart("dir", disDirBody).build();
    //
    //
    //        httpPost.setEntity(httpEntity);
    //
    //        // 发起请求 并返回请求的响应
    //        response = httpClient.execute(httpPost);
    //
    //        // 获取响应对象
    //        HttpEntity resEntity = response.getEntity();
    //        if (resEntity != null) {
    //            // 打印响应长度
    //            logger.info("Response content length: " + resEntity.getContentLength());
    //            // 打印响应内容
    //            logger.info(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
    //        }
    //
    //        // 销毁
    //        EntityUtils.consume(resEntity);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    } finally {
    //        try {
    //            if (response != null) {
    //                response.close();
    //            }
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //
    //        try {
    //            if (httpClient != null) {
    //                httpClient.close();
    //            }
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}
}
