package com.weather.bigdata.it.cluster.util;

import com.weather.bigdata.it.utils.hdfsUtil.HDFSFile;
import com.weather.bigdata.it.utils.hdfsUtil.HDFSOperation1;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


/**
 * properties 操作
 * 
 * @author zhichen
 * 
 *         java读取properties文件有很多方法，看有人整理了如下7种。
 * 
 *         其实很多都是大同小异，概括起来就2种：
 * 
 *         1、先构造出一个InputStream来，然后调用Properties#load()
 * 
 *         2、利用ResourceBundle，这个主要在做国际化的时候用的比较多。
 *         例如：它能根据系统语言环境自动读取下面三个properties文件中的一个：
 * 
 *         resource_en_US.properties
 * 
 *         resource_zh_CN.properties
 * 
 *         resource.properties
 * 
 * 
 *         附上别人整理的6中方法...
 * 
 *         1、使用java.util.Properties类的load()方法
 * 
 *         InputStream in = new BufferedInputStream(new FileInputStream(name));
 * 
 *         Properties p = new Properties();
 * 
 *         p.load(in);
 * 
 *         2、使用java.util.ResourceBundle类的getBundle()方法
 * 
 *         ResourceBundle rb = ResourceBundle.getBundle(name,
 *         Locale.getDefault());
 * 
 *         3、使用java.util.PropertyResourceBundle类的构造函数
 * 
 *         InputStream in = new BufferedInputStream(new FileInputStream(name));
 * 
 *         ResourceBundle rb = new PropertyResourceBundle(in);
 * 
 *         4、使用class变量的getResourceAsStream()方法
 * 
 *         InputStream in =
 *         JProperties.class.getResourceAsStream(name);//JProperties为当前类名
 * 
 *         Properties p = new Properties();
 * 
 *         p.load(in);
 * 
 *         5、使用class.getClassLoader()所得到的java.lang.
 *         ClassLoader的getResourceAsStream()方法
 * 
 *         InputStream in =
 *         JProperties.class.getClassLoader().getResourceAsStream(name);
 * 
 *         Properties p = new Properties();
 * 
 *         p.load(in);
 * 
 *         6、使用java.lang.ClassLoader类的getSystemResourceAsStream()静态方法
 * 
 *         InputStream in = ClassLoader.getSystemResourceAsStream(name);
 * 
 *         Properties p = new Properties();
 * 
 *         p.load(in);
 * 
 *         7、在Servlet中可以使用javax.servlet.ServletContext的getResourceAsStream()方法
 * 
 *         InputStream in = context.getResourceAsStream(path);
 * 
 *         Properties p = new Properties();
 * 
 *         p.load(in);
 *
 */
public class PropertiesUtils {

    public static Properties readProperties1(String filename){
        System.out.println("加载filename="+filename);
        Properties prop=new Properties();
        try {
            prop.load(HDFSFile.fileInputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

	/**
	 * 读取资源文件,并处理中文乱码
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static Properties readProperties(String filename) {
		return readProperties(filename, null);
	}

	/**
	 * 读取资源文件,并处理中文乱码
	 * 
	 * @param filename
	 *            文件名
	 * @param charsetName
	 *            文件编码
	 * @return
	 */
	public static Properties readProperties(String filename, String charsetName) {
		if (StringUtils.isEmpty(charsetName)) {
			charsetName = "UTF-8";
		}
		Properties properties = new Properties();
		BufferedReader bf = null;
		try {
			/*
			 * getResourceAsStream读取的文件路径只局限与工程的源文件夹中，包括在工程src根目录下，以及类包里面任何位置，
			 * 但是如果配置文件路径是在除了源文件夹之外的其他文件夹中时，该方法是用不了的。
			 */
			 InputStream inputStream =
			 PropertiesUtils.class.getClassLoader().getResourceAsStream(filename);
			// 可以读取外部文件，参数需要使用绝对路径
//			InputStream inputStream = new FileInputStream(filename);
			bf = new BufferedReader(new InputStreamReader(inputStream,
					charsetName));
			properties.load(bf);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 写资源文件，含中文
	 * 
	 * @param filename
	 *            文件名
	 * @param properties
	 *            properties属性
	 */
	public static void writeProperties(String filename, Properties properties) {
		writeProperties(filename, properties, null);
	}

	/**
	 * 写资源文件，含中文
	 * 
	 * @param filename
	 *            文件名
	 * @param properties
	 *            properties属性
	 * @param charsetName
	 *            文件编码
	 */
	public static void writeProperties(String filename, Properties properties,
			String charsetName) {
		if (StringUtils.isEmpty(charsetName)) {
			charsetName = "UTF-8";
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), charsetName));
			properties.store(bw, "Copyright (c) zhichen");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 修改properties
	 *
	 * @param filename
	 * @param update
	 */
	public static void updateProperty(String filename,
			Map<String, String> update) {
		updateProperty(filename, update, null);
	}

	/**
	 * 修改properties
	 * 
	 * @param filename
	 * @param update
	 * @param charsetName
	 */
	public static void updateProperty(String filename,
			Map<String, String> update, String charsetName) {
		if (StringUtils.isEmpty(charsetName)) {
			charsetName = "UTF-8";
		}
		Properties properties = readProperties(filename);
		for (Entry<String, String> entry : update.entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
		}
		writeProperties(filename, properties, charsetName);
	}

	/**
	 * 
	 * @param properties
	 */
	public static void showKeys(Properties properties) {
		Enumeration<?> enu = properties.propertyNames();
		while (enu.hasMoreElements()) {
			Object key = enu.nextElement();
			System.out.println(key);
		}
	}

	/**
	 * @param properties
	 */
	public static void showValues(Properties properties) {
		Enumeration<Object> enu = properties.elements();
		while (enu.hasMoreElements()) {
			Object value = enu.nextElement();
			System.out.println(value);
		}
	}

	/**
	 * @param properties
	 */
	public static void showKeysAndValues(Properties properties) {
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(key + "=" + value);
		}
	}
	
}
