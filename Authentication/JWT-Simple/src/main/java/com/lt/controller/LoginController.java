package com.lt.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lt.config.TokenUtils;
import com.lt.config.AuthenticationInterceptor;
import com.lt.config.PassToken;
import com.lt.config.TokenValidated;
import com.lt.model.AuthenticationRequest;
import com.lt.model.UserDto;
import com.lt.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangtao
 * @description
 * @date 2021年07月21 14:58
 **/
@RestController
public class LoginController {
    @Autowired
    AuthenticationService authenticationService;

    @PassToken
    @PostMapping("/login")
    public Object login(AuthenticationRequest user) {
        UserDto userDto = authenticationService.authentication(user);
        String token = TokenUtils.getToken(userDto);
        JSONObject jsonObj = JSONUtil.createObj();
        jsonObj.putOpt("token", token).putOpt("msg", "登录成功");
        return jsonObj;
    }

    @TokenValidated
    @GetMapping("/getMessage")
    public String getMessage(@RequestHeader("token") String token) {
        System.out.println("token: " + token);
        UserDto userDto = AuthenticationInterceptor.currentUser.get();
        return "你已通过验证: " + userDto;
    }

}
