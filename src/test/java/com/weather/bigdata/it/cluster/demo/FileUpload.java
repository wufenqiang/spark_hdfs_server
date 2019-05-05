//package com.weather.bigdata.it.cluster.controller;
////
////import java.io.File;
////import java.io.IOException;
////import java.nio.charset.Charset;
////
////import org.apache.commons.lang3.StringUtils;
////import org.apache.http.Consts;
////import org.apache.http.HttpEntity;
////import org.apache.http.client.methods.CloseableHttpResponse;
////import org.apache.http.client.methods.HttpPost;
////import org.apache.http.entity.ContentType;
////import org.apache.http.entity.mime.HttpMultipartMode;
////import org.apache.http.entity.mime.MultipartEntityBuilder;
////import org.apache.http.entity.mime.content.FileBody;
////import org.apache.http.entity.mime.content.StringBody;
////import org.apache.http.impl.client.CloseableHttpClient;
////import org.apache.http.impl.client.HttpClients;
////import org.apache.http.util.EntityUtils;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestMethod;
////import org.springframework.web.bind.annotation.RestController;
////
////@RestController
////@EnableAutoConfiguration
////public class FileUpload {
////
////	private static Logger logger = LoggerFactory.getLogger(FileUpload.class);
////
////	/**
////	 * 文件上传
////	 *
////	 * @param file
////	 *            本地文件全路径
////	 * @param url
////	 * @param callback
////	 * @return
////	 */
////	@RequestMapping(value = "/file/up", method = RequestMethod.GET)
////	public static void uploadFile(String file, String url, String disDir) {
////		url = "http://localhost:8082/file/upload";
////		disDir="/Users/liweifeng/test/filetest";
////		CloseableHttpClient httpClient = null;
////		CloseableHttpResponse response = null;
////		try {
////			httpClient = HttpClients.createDefault();
////
////			// 把一个普通参数和文件上传给下面这个地址 是一个servlet
////			HttpPost httpPost = new HttpPost(url);
////
////			// 把文件转换成流对象FileBody
////			File localFile = new File(file);
////			System.out.println("本地文件大小："+localFile.length());
////			FileBody bin = new FileBody(localFile);
////
////			// StringBody userName = new StringBody("Scott",
////			// ContentType.create("text/plain", Consts.UTF_8));
////			// StringBody password = new StringBody("123456",
////			// ContentType.create("text/plain", Consts.UTF_8));
////			StringBody disDirBody = new StringBody(disDir, ContentType.create("text/plain", Consts.UTF_8));
////
////			HttpEntity httpEntity = MultipartEntityBuilder.create()
////					.setCharset(Charset.forName("utf-8"))
////					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
////					// 相当于<input type="file" name="file"/>
////					.addPart("file", bin)
////
////					// 相当于<input type="text" name="userName" value=userName>
////					// .addPart("userName", userName).addPart("pass",
////					// password).build();
////					.addPart("dir", disDirBody).build();
////
////			httpPost.setEntity(httpEntity);
////
////			// 发起请求 并返回请求的响应
////			response = httpClient.execute(httpPost);
////
////			// 获取响应对象
////			HttpEntity resEntity = response.getEntity();
////			if (resEntity != null) {
////				// 打印响应长度
////				logger.info("Response content length: " + resEntity.getContentLength());
////				// 打印响应内容
////				logger.info(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
////			}
////
////			// 销毁
////			EntityUtils.consume(resEntity);
////		} catch (Exception e) {
////			e.printStackTrace();
////		} finally {
////			try {
////				if (response != null) {
////					response.close();
////				}
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////
////			try {
////				if (httpClient != null) {
////					httpClient.close();
////				}
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////		}
////	}
////
////
////}
