package com.lt;

import com.lt.annotation.EnableMethodCostTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liangtao
 * @description 通过注解 {@link EnableMethodCostTime}  来导入service方法执行时间统计功能
 * @date 2021年06月29 13:25
 **/
@SpringBootApplication
@EnableMethodCostTime
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
