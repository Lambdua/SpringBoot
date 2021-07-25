package com.lt.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

/**
 * JWTUserDetailsS​​ervice 实现了 Spring Security UserDetailsS​​ervice 接口。
 * 它重写了`loadUserByUsername`以使用用户名从数据库中获取用户详细信息。
 * Spring Security Authentication Manager 在验证用户提供的用户详细信息时调用此方法从数据库中获取用户详细信息。
 * 在这里，我们硬编码用户详细信息,实际使用中，我们通过注入数据库service来从数据库查询，或者redis等缓存中查找。
 * 此外，用户的密码使用 BCrypt 以加密格式存储,这里通过在线生成，地址：https://www.javainuse.com/onlineBcrypt
 * 用户： liagtao
 * 密码： 123456
 * @author liangtao
 * @date 2021/7/22
 **/
public class CustUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("zhangsan".equals(username)) {
            return new User("zhangsan", "$2a$10$r9i69zynZbxFmf/zgBacNOP3U8FOC5a00rFMbIvaZNxMvqZf7ax0q",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
