package com.lt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author liangtao
 * @description 登录跳转接口，这里加不加`/`路由都可，默认是在 resource/static/目录下
 * @date 2021年07月22 16:56
 **/
@Controller
public class LoginController {

    /**
     * 登录成功后默认跳转接口，这里是get请求
     *
     * @param authentication 可以通过参数注入，直接获取认证对象
     * @return java.lang.String  直接返回指定页面
     * @author liangtao
     * @date 2021/7/25
   k **/
    @GetMapping("/defaultSuccess")
    public String defaultSuccess(Authentication authentication) {
        System.out.println(authentication.getPrincipal());
        System.out.println("登录的用户事： " + authentication.getName());
        return "/main.html";
    }

    /**
     * 登录成功重定向接口，这里必须是post请求
     *
     * @param authentication2 这里是拿不到这个对象的,必须通过 contextHolder 去获取认证对象。
     * @return java.lang.String  重定向url必须用 redirect声明
     * @author liangtao
     * @date 2021/7/25
     **/
    @PostMapping(value = "/loginSuccess")
    public String successRedirectUrl(Authentication authentication2) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println(authentication.getPrincipal());
        System.out.println("登录的用户事： " + authentication.getName());
        return "redirect:main.html";
    }

    /**
     * 登录失败重定向
     * @author liangtao
     * @date 2021/7/25
     * @return java.lang.String
     **/
    @PostMapping("/loginFailure")
    public String loginFailureUrl() {
        return "redirect:/loginError.html";
    }

    /**
     * 登录失败跳转
     * @author liangtao
     * @date 2021/7/25
     * @param
     * @return java.lang.String
     **/
    @GetMapping("/defaultLoginFailure")
    public String defaultLoginFailureUrl() {
        return "loginError.html";
    }


}
