package com.lt.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lt.model.UserDto;

import java.util.Date;

/**
 * @author liangtao
 * @description
 * @date 2021年07月21 14:33
 **/
public class TokenUtils {
    /**
     * 获取token
     * Algorithm.HMAC256():使用HS256生成token,密钥则是用户的密码，唯一密钥的话可以保存在服务端。
     * withAudience()存入需要保存在token的信息,负载
     *
     * @author liangtao
     * @date 2021/7/21
     * @param userDto 用户实体
     * @return java.lang.String
     **/
    public static String getToken(UserDto userDto) {
        return JWT.create().withAudience(JSONUtil.toJsonStr(userDto))
                .withClaim("自定义声明",true)
                //有效时间两小时
                .withExpiresAt(DateUtil.offsetHour(new Date(),2))
                .withSubject("签发人：涛")
                .sign(Algorithm.HMAC256(userDto.getPassword()));
    }
}
