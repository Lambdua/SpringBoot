package com.lt.config;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lt.model.UserDto;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author liangtao
 * @description JWT鉴权拦截器
 * @date 2021年07月21 14:39
 **/
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static final String TOKEN_HEADER = "token";

    public static final ThreadLocal<UserDto> currentUser = new NamedThreadLocal<>("UserThread");

    /**
     * 在请求前拦截判断是否需要鉴权，以及进行权限校验
     *
     * @return boolean
     * @author liangtao
     * @date 2021/7/21
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //不是方法处理，跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            return true;
        }
        if (method.isAnnotationPresent(TokenValidated.class)) {
            String token = request.getHeader(TOKEN_HEADER);
            if (token == null) {
                throw new RuntimeException("获取token失败");
            }
            //获取当前用户
            DecodedJWT jwt = JWT.decode(token);
            if (jwt.getExpiresAt().before(new Date())) {
                throw new RuntimeException("token过期");
            }
            String userJson = jwt.getAudience().get(0);
            UserDto userDto = JSONUtil.toBean(userJson, UserDto.class);
            currentUser.set(userDto);
            //验证token
            Algorithm algorithm;
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userDto.getPassword()))
//                    .withClaim("自定义声明", true)
                    .build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                throw new RuntimeException("token验签失败，401");
            }
            return true;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {
        currentUser.remove();
    }
}
