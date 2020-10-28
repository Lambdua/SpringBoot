package com.lt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @author liangtao
 * @date 2020/10/28
 **/
@Data
@AllArgsConstructor
public class UserDto {
    public static final String SESSION_USER_KEY = "_user";
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String mobile;
    /**
     * 用户权限
     */
    private Set<String> authorities;
}
