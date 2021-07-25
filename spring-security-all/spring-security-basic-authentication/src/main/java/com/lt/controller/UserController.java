package com.lt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liangtao
 * @description
 * @date 2021年07月22 16:56
 **/
@RestController
public class UserController {

    /**
     * 登录成功后默认跳转接口，这里是get请求
     * @author liangtao
     * @date 2021/7/25
     * @param authentication 可以通过参数注入，直接获取认证对象
     * @return java.lang.String  直接返回指定页面
     **/
    @GetMapping("/defaultSuccess")
    public String defaultSuccess(Authentication authentication){
        System.out.println(authentication.getPrincipal());
        System.out.println("登录的用户事： "+authentication.getName());
        return "/main.html";
    }

    /**
     * 登录成功重定向接口，这里必须是post请求
     * @author liangtao
     * @date 2021/7/25
     * @param authentication2 这里是拿不到这个对象的， 必须通过contextHoldre去获取认证对象。
     * @return java.lang.String  重定向url必须用 redirect声明
     **/
    @PostMapping(value = "/user/getUserList", produces = "application/json")
    public String getUserList(HttpServletRequest request,Authentication authentication2) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println(authentication.getPrincipal());
        System.out.println("登录的用户事： "+authentication.getName());
        return "redirect:/main.html";
//        return UserDto.createList(10);
    }


}
