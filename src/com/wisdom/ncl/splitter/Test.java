package com.wisdom.ncl.splitter;


public class Test {
	public static void main(String[] args) {
		// ����log4j.properties�ļ���·��
		// PropertyConfigurator.configure(System.getProperty("user.dir") +
		// "/config/log4j.properties");
		Splitter splitter = new Splitter();
		splitter.startService();
	}
}