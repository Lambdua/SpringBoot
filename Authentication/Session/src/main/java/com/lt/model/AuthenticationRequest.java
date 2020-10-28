package com.lt.model;

import lombok.Data;

/**
 * @author liangtao
 * @date 2020/10/28
 **/
@Data
public class AuthenticationRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
