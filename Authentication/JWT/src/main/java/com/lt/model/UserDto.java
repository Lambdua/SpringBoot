package com.lt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liangtao
 * @date 2020/10/28
 **/
@Data
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String mobile;

}
