package com.wisdom.ncl.splitter.tools;

/**
 * Copyright (C), 2011-2012, beijing ow Co., Ltd.
 * �ļ���:     PropertiesTools.java
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * ������: properties ������ 
 * �޸���ʷ:
 * 
 * @author yuhao
 * @version 1.0
 * @date 2011-9-13 ����12:55:53 
 * ������
 * 
 */

public class PropertiesTools {

	/**
	 * 
	 * ������: getProperties ��������: ����·����ȡ Properties �޸���ʷ:
	 * 
	 * @date 2011-9-13 ����01:29:39
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param filePath
	 *            ��Ҫ�ӹ��̵ĸ�·����ʼд ��ʽΪ��com/ow/test/test.properties
	 * @return Properties����
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
			System.out.println("�Բ���properties�ļ�·������ȷ��");
		}
		return prop;
	}
	/**
	 * 
	 * ������:    getValue
	 * ��������:  ����key��ȡ��Ӧ��ֵ  
	 * �޸���ʷ:    
	 * @date      2011-9-13 ����01:31:09
	 * @author    yuhao
	 * @version   1.0
	 * @description
	 * ���롢�������:    
	 * @param filePath proeprties�ļ�·��
	 * @param propKey Ҫ��ѯ����
	 * @return ��ѯ��ֵ��Ӧ��ֵ
	 */
	public static synchronized String getValue(String filePath, String propKey) {
		String value = "";
		Properties prop = getProperties(filePath);
		boolean flag = prop.containsKey(propKey);
		if (flag) {
			value = (String) prop.get(propKey);
		} else {
			System.out.println("�Բ����ļ��ڲ��������������key:"+propKey);
		}
		return value;
	}

	/**
	 * 
	 * ������:    main
	 * ��������:  ���Է���  
	 * �޸���ʷ:    
	 * @date      2011-9-13 ����01:32:18
	 * @author    yuhao
	 * @version   1.0
	 * @description
	 * ���롢�������:    
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getValue("com/ow/test/test.properties", "sex"));
	}
}
