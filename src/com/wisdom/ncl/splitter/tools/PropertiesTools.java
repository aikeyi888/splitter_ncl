package com.wisdom.ncl.splitter.tools;

/**
 * Copyright (C), 2011-2012, beijing ow Co., Ltd.
 * 文件名:     PropertiesTools.java
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 类描述: properties 工具类 
 * 修改历史:
 * 
 * @author yuhao
 * @version 1.0
 * @date 2011-9-13 下午12:55:53 
 * 其它：
 * 
 */

public class PropertiesTools {

	/**
	 * 
	 * 函数名: getProperties 功能描述: 根据路径获取 Properties 修改历史:
	 * 
	 * @date 2011-9-13 下午01:29:39
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param filePath
	 *            需要从工程的根路径开始写 格式为：com/ow/test/test.properties
	 * @return Properties对象
	 */
	public static synchronized Properties getProperties(String filePath) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(filePath);
		Properties prop = new Properties();
		InputStream is = null;
		try {
			is = url.openStream();
			prop.load(is);
		} catch (IOException e) {
			System.out.println("对不起，properties文件路径不正确。");
		}
		return prop;
	}
	/**
	 * 
	 * 函数名:    getValue
	 * 功能描述:  根据key获取对应的值  
	 * 修改历史:    
	 * @date      2011-9-13 下午01:31:09
	 * @author    yuhao
	 * @version   1.0
	 * @description
	 * 输入、输出参数:    
	 * @param filePath proeprties文件路径
	 * @param propKey 要查询的名
	 * @return 查询的值对应的值
	 */
	public static synchronized String getValue(String filePath, String propKey) {
		String value = "";
		Properties prop = getProperties(filePath);
		boolean flag = prop.containsKey(propKey);
		if (flag) {
			value = (String) prop.get(propKey);
		} else {
			System.out.println("对不起，文件内不包含你所请求的key:"+propKey);
		}
		return value;
	}

	/**
	 * 
	 * 函数名:    main
	 * 功能描述:  测试方法  
	 * 修改历史:    
	 * @date      2011-9-13 下午01:32:18
	 * @author    yuhao
	 * @version   1.0
	 * @description
	 * 输入、输出参数:    
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getValue("com/ow/test/test.properties", "sex"));
	}
}
