package org.company.main;

import org.company.common.utils.ResourceBundlesScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	
	ResourceBundlesScanner scanner;
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		Test myTest = new Test();
		myTest.printMessage(context);
	}
	
	private void printMessage(ApplicationContext context ){
		scanner =(ResourceBundlesScanner)context.getBean("resourceBundleScanner");
		System.out.println(String.format("Key is %s and its value is %s ","NAME",scanner.getMessage("NAME")));
		
		//When property is not defined..return the default value
		System.out.println(String.format("Key is %s and its DEFAULT value is %s ","NAME",scanner.getMessageOrDefault("id", "94914")));
		
		System.out.println(String.format("Key is %s and its value is %s ","ID",scanner.getMessage("ID")));
		
		System.out.println(String.format("Key is %s and its value is %s ","ROLE",scanner.getMessage("ROLE")));
	}
}
