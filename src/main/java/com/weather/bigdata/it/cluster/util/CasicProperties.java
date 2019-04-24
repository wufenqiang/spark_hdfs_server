package com.weather.bigdata.it.cluster.util;

import java.util.Properties;

/**
 * 配置文件读取工具类
 * @author zhichen
 *
 */
public class CasicProperties {
	
	/*
	 * 系统数据配置文件
	 */
	public static final Properties CONFPROPERTIES = PropertiesUtils.readProperties("config.properties");

}
