package com.lt.redis.config;

import com.lt.redis.interceptor.ClickCollectInterceptor;
import com.lt.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liangtao
 * @description 拦截器配置方式2,推荐使用
 * @date 2021年05月25 11:36
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    RedisRepository redisRepository;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ClickCollectInterceptor(redisRepository)).addPathPatterns("/**");
    }


}
