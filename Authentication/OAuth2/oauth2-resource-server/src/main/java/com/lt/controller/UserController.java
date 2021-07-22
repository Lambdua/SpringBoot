package com.lt.controller;

import com.lt.model.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liangtao
 * @description
 * @date 2021年07月22 16:56
 **/
@RestController
public class UserController {
    @GetMapping(value = "/user/getUserList", produces = "application/json")
    public List<UserDto> getUserList() {
        return UserDto.createList(10);
    }

}
