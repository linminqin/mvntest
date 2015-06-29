package com.lmiky.platform.test;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 测试积累
 * @author lmiky
 * @date 2013-4-15
 */
@ContextConfiguration(locations={
		"classpath:config/applicationContext*.xml"})
public class BaseTest extends AbstractJUnit4SpringContextTests {
	public BaseTest() {
		PropertyConfigurator.configure(BaseTest.class.getClassLoader().getResource("config/log4j.properties")); 
	}
	public static void main(String[] args) {
		PropertyConfigurator.configure(BaseTest.class.getClassLoader().getResource("config/log4j.properties")); 
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config/applicationContext*.xml");  
		System.out.println(applicationContext);
	}
}
