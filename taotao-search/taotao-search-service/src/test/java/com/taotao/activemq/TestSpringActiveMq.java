package com.taotao.activemq;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringActiveMq {
	
	@Test
	public void testSpringActiveMq() throws Exception{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		System.in.read();
		
	}
	

}
