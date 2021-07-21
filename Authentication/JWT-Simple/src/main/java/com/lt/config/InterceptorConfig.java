package com.lt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liangtao
 * @description 配置拦截器
 * @date 2021年07月21 14:56
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public HandlerInterceptor authenticationTokenInterceptor(){
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationTokenInterceptor())
                .addPathPatterns("/**");
    }
}
