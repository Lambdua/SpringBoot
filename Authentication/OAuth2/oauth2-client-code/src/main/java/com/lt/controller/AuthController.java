package com.lt.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.lt.model.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author liangtao
 * @description
 * @date 2021年07月22 17:24
 **/
@Controller
public class AuthController {

    private static final Integer STATE = 12345;

    //认证服务地址 获取验证码
    private static final String SERVER_AUTHENTICATION_ADDR = "http://localhost:1112/oauth/authorize";

    private static final String CLIENT_ID = "javainuse";

    private static final String CLIENT_SECRET = "secret";
    private static final String GRANT_TYPE = "authorization_code";

    private static final String CALL_BACK_GET_CODE_URL ="http://localhost:1111/callBackGetCode";
    //授权服务地址 获取token
    private static final String SERVER_AUTHORIZATION_ADDR = "http://localhost:1112/oauth/token?" +
//            "client_id=%s&" +
//            "client_secret=%s&" +
            "code=%s&" +
            "grant_type=%s&" +
            "redirect_uri=%s";

    private static final String getUserListUrl="http://localhost:1112/user/getUserList";

    /**
     * 申请认证，访问服务端认证服务器，进行认证
     *
     * @return org.springframework.web.servlet.ModelAndView
     * @author liangtao
     * @date 2021/7/23
     **/
    @RequestMapping(value = "/applyAuthentication", method = RequestMethod.GET)
    public ModelAndView applyAuthentication() {
        ModelAndView modelAndView = new ModelAndView("applyAuthentication");
        //生成的随机数
        modelAndView.addObject("state", STATE);
        //回调地址
        modelAndView.addObject("redirect_uri", CALL_BACK_GET_CODE_URL);
        //验证码模式
        modelAndView.addObject("response_type", "code");
        //客户端申请的id,这里我们假设已经申请了id
        modelAndView.addObject("client_id", CLIENT_ID);
        //请求资源范围
        modelAndView.addObject("scope", "read");
        //服务端验证码申请地址
        modelAndView.addObject("serverAuthenticationAddr", SERVER_AUTHENTICATION_ADDR);
        return modelAndView;
    }

    /**
     * 认证服务器回调的客户端路由地址，给客户端code码。通过code向服务端获取token
     * 通过token获取用户信息
     *
     * @param code 验证码
     * @author liangtao
     * @date 2021/7/23
     **/
    @GetMapping("/callBackGetCode")
    public ModelAndView callBackGetCode(@RequestParam("code") String code, @RequestParam("state") Integer state) {
        if (!state.equals(STATE)) {
            throw new IllegalStateException("state illegal: " + state);
        }
        System.out.println("Authorization Code------" + code);

        String getTokenUrl = String.format(SERVER_AUTHORIZATION_ADDR, code, GRANT_TYPE,CALL_BACK_GET_CODE_URL);
        System.out.println("getTokenUrl = " + getTokenUrl);
        HttpResponse response = HttpRequest
                .post(getTokenUrl)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
                .basicAuth(CLIENT_ID, CLIENT_SECRET)
                .execute();
        if (response.isOk()) {
            //{"access_token":"7ea6df34-c704-4570-8463-27a258686149","token_type":"bearer","expires_in":43199,"scope":"read"}
            String responseStr = response.body();
            System.out.println("get token success," + responseStr);
            String token = JSONUtil.parseObj(responseStr).getStr("access_token");
            HttpResponse userListResponse = HttpRequest.get(getUserListUrl).auth("Bearer " + token).execute();
            if (userListResponse.isOk()){
                List<UserDto> userList = JSONUtil.toList(JSONUtil.parseArray(userListResponse.body()), UserDto.class);
                System.out.println("userList: "+userList);
                ModelAndView modelAndView = new ModelAndView("showUserList");
                modelAndView.addObject("userList",userList);
                return modelAndView;
            }else {
                System.out.println("get userList failure,status:"+userListResponse.getStatus()+",cause: " + userListResponse.body());
            }
        } else {
            System.out.println("get token failure,cause: " + response.body());
        }
        return null;
    }
}
