package com.lt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liangtao
 * @Date 2020/6/10
 **/
@SpringBootApplication
@EnableFeignClients
public class ConsumerFeign80 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeign80.class, args);
    }
}
