package com.lt.controller;

import com.lt.model.AuthenticationRequest;
import com.lt.model.UserDto;
import com.lt.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author 梁先生
 * @description 用户登录Controller
 * @Date 2020/10/28
 **/
@RestController
public class LoginController {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", produces = "text/plain;charset=utf-8")
    public String login(AuthenticationRequest authenticationRequest, HttpSession session) {
        UserDto userDto = authenticationService.authentication(authenticationRequest);
        //存入session
        session.setAttribute(UserDto.SESSION_USER_KEY, userDto);
        return userDto.getUsername() + "登录成功";
    }

    @GetMapping(value = "/logout", produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession session) {
        session.invalidate();
        return "退出成功";
    }

}
