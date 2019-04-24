package com.weather.bigdata.it.cluster.util;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * @author zhichen
 * @editor wufenqiang
 *
 */
public class StringTextUtils {

	/**
	 * 将content内容存储到toTxtFileName中
	 * 
	 * @param toTxtFileName
	 *            写入的文件名
	 * @param content
	 *            内容
	 * @return
	 */
	public static void writeStringToFile(String toTxtFileName, String content) {
		writeStringToFile(toTxtFileName, content, null);
	}

	/**
	 * 将content内容存储到toTxtFileName中
	 * 
	 * @param toTxtFileName
	 *            写入的文件名
	 * @param content
	 *            内容
	 * @param charsetName
	 *            文件编码
	 * @return
	 */
	public static void writeStringToFile(String toTxtFileName, String content,
			String charsetName) {
		if (charsetName==null||"".equals(charsetName)) {
			charsetName = "UTF-8";
		}
		File file = new File(toTxtFileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(toTxtFileName, false), charsetName));
			bw.write(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
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
	 * 读取文本内容放入字符串中
	 * 
	 * @param sourceFile
	 *            文件名
	 * @return
	 */
	public static String readFileToString(String sourceFile) {
		return readFileToString(sourceFile, null);
	}
	
	/**
	 * 读取文本内容放入字符串中
	 * 
	 * @param sourceFile
	 *            文件名
	 * @param charset
	 * 	                                 文件编码
	 * @return
	 */
	public static String readFileToString(String sourceFile, String charset) {
		if (charset==null||"".equals(charset)) {
			charset = "UTF-8";
		}
		BufferedReader br = null;
		String lineStr = null;
		StringBuffer bf = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					sourceFile), charset));
			while ((lineStr = br.readLine()) != null) {
				bf.append(lineStr);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bf.toString();
	}
	
	/**
	 * 分行读取文件，存放到list中，list中每行一条记录
	 * 
	 * @param sourceFile
	 *            文件名
	 * @return
	 */
	public static List<String> readFileToList(String sourceFile) {
		return readFileToList(sourceFile, null);
	}
	
	/**
	 * 分行读取文件，存放到list中，list中每行一条记录
	 * 
	 * @param fileName
	 *            文件名
	 * @param charsetName
	 *            文件编码
	 * @return
	 */
	public static List<String> readFileToList(String fileName, String charsetName) {
		if (charsetName == null || "".equals(charsetName)) {
			charsetName = "UTF-8";
		}
		BufferedReader br = null;
		String lineStr = null;
		List<String> contents = new ArrayList<String>();
		try {
			/*
			 * getResourceAsStream读取的文件路径只局限与工程的源文件夹中，包括在工程src根目录下，以及类包里面任何位置，
			 * 但是如果配置文件路径是在除了源文件夹之外的其他文件夹中时，该方法是用不了的。
			 */
			InputStream inputStream = StringTextUtils.class.getClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) {
				// 可以读取外部文件，参数需要使用绝对路径
				inputStream = new FileInputStream(fileName);
			}
			br = new BufferedReader(new InputStreamReader(inputStream, charsetName));
			while ((lineStr = br.readLine()) != null) {
				contents.add(lineStr);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return contents;
	}

}
