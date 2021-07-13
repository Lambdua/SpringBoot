package com.lt;

import com.lt.bean.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liangtao
 * @description
 * @Date 2021/6/19
 **/
public class OriginLearn {
    public static void main(String[] args) {
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext("com.lt");
//        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-config.xml");
        Bean a = (Bean) applicationContext.getBean("bean");
        System.out.println(a.name);

    }
}
