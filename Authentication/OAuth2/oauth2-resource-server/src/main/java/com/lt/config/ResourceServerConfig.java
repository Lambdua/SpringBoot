package com.lt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author liangtao
 * @description 资源获取配置类,用于配置哪些系统资源需要什么样的角色权限等
 * 这里也可以继承{@link org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter}
 * @date 2021年07月23 11:10
 **/
@Configuration
@EnableResourceServer
@Order(3)
public class ResourceServerConfig implements ResourceServerConfigurer {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/user/getUserList/**")
                .and().authorizeRequests().anyRequest().access("#oauth2.hasScope('read')");
    }
}
