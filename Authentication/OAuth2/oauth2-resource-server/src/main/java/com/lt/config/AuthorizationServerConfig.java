package com.lt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author liangtao
 * @description 授权服务器配置
 * @date 2021年07月22 17:06
 **/
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 这里的 configure 方法注入了 Spring Security 认证管理器。 使用内存客户端服务，我们设置了可以访问服务器的客户端。
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("javainuse").secret("secret")
                //授权码模式
                .authorizedGrantTypes("authorization_code")
                .scopes("read").authorities("CLIENT");
    }
}
