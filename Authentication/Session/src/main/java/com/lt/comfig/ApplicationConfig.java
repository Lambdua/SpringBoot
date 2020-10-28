package com.lt.comfig;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 *
 * @author 梁先生
 * @Date 2020/10/27
 * 再此配置除了Controller的其它bean,比如：数据库链接池，事务管理、业务bean等
 **/
@Configurable
@ComponentScan(
        basePackages = "com.lt",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Controller.class)}
)
public class ApplicationConfig {
}
