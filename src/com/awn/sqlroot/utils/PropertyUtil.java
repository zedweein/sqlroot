package com.awn.sqlroot.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * PropertiesUtil.java
 * 
 * @desc properties 资源文件解析工具
 * @author Awn
 * 
 */
public class PropertyUtil {
	
	private Properties props;
	private URI uri;

	public PropertyUtil(String fileName) {
		fileName = "/"+fileName;
		try {
			props = new Properties();
			InputStream fis = getClass().getResourceAsStream(fileName);
			props.load(fis);
			uri = this.getClass().getResource(fileName).toURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取某个属性
	 */
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	/**
	 * 获取所有属性，返回一个map,不常用 可以试试props.putAll(t)
	 */
	public Map<String, String> getAllProperty() {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> enu = props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			//目前开放arcgis的相关配置
			if(key.startsWith("arcgis")||key.contains("server-context")){
				String value = props.getProperty(key);
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 在控制台上打印出所有属性，调试时用。
	 */
	public void printProperties() {
		props.list(System.out);
	}

	/**
	 * 写入properties信息
	 */
	public void writeProperties(String key, String value) {
		try {
			OutputStream fos = new FileOutputStream(new File(uri));
			props.setProperty(key, value);
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			props.store(fos, "『comments』Update key：" + key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}