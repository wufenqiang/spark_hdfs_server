package com.weather.bigdata.it.cluster.controller;

import com.alibaba.fastjson.JSON;
import com.weather.bigdata.it.cluster.util.CasicProperties;
import com.weather.bigdata.it.spark.sparksubmit.sparksubmit_PropertiesUtil;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSConfUtil;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSFile;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSOperation1;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@EnableAutoConfiguration
public class FileController {

	private static Logger logger = LoggerFactory.getLogger(FileController.class);
    private static Properties prop = CasicProperties.CONFPROPERTIES;

	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public Object uploadFile(HttpServletRequest req, @RequestParam("file") MultipartFile file,String user,String password,String regionKey,String dataType,String callback){
        String remoteHost=req.getRemoteHost();
        String remoteAddr=req.getRemoteAddr();
        int remotePort=req.getRemotePort();
        String remoteUser=req.getRemoteUser();

	    //System.out.println(password);
        String upload_dir = prop.getProperty("upload_dir");
        logger.info(user+"发送了上传文件【{"+regionKey+","+dataType+"}】的请求。",file.getName()+"->"+upload_dir,"remoteUser("+remoteUser+"),remoteHost("+remoteHost+":"+remotePort+"),remoteAddr("+remoteAddr+")");


        if (req.getCharacterEncoding() == null) {
            try {
                req.setCharacterEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        logger.info("文件上传目录:{}",upload_dir);
        return this.upload(file,upload_dir,regionKey,dataType,callback);
    }
    private Object upload(@RequestParam("file") MultipartFile file,String upload_dir0,String regionKey,String dataType,String callback) {


		Map<String, String> result = new HashMap<String, String>();

		String upload_dir1= HDFSConfUtil.formatFilename(upload_dir0);
        String upload_dir=upload_dir1;
        if(dataType.equals("mt_commonsLib")){
            upload_dir=sparksubmit_PropertiesUtil.getmt_commonsLibPath_regionKey(regionKey);
        }else{
            upload_dir=sparksubmit_PropertiesUtil.getsignallibPath_dataType_regionKey(dataType,regionKey);
        }



        if(!HDFSOperation1.exists(upload_dir)){
            HDFSOperation1.mkdirs(upload_dir);
        }

		//目标文件

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
		logger.info("用户请求上传文件，服务器返回结果为：{}。", JSON.toJSONString(result));
		if (!StringUtils.isEmpty(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}
	}


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
