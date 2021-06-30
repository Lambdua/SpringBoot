package com.lt.annotation;

import com.lt.config.MethodCostTimeProxyBeanPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liangtao
 * @date 2020/11/24
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MethodCostTimeProxyBeanPostProcessor.class)
public @interface EnableMethodCostTime {
}
