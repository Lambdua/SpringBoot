package com.lt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt库工具
 * @author liangtao
 * */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60L;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 获取用户名，这里拿的是subject
     * @author liangtao
     * @date 2021/7/22
     * @param token token
     * @return java.lang.String
     **/
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 获取过期时间
     * @author liangtao
     * @date 2021/7/22
     * @param token token
     * @return java.util.Date
     **/
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 获取荷载中的指定声明
     * @author liangtao
     * @date 2021/7/22
     * @param token token
     * @param claimsResolver 声明解析函数
     * @return T
     **/
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取所有的token荷载信息,需要用到密钥
     * @author liangtao
     * @date 2021/7/22
     * @param token token
     * @return io.jsonwebtoken.Claims
     **/
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 检查token是否过期
     * @author liangtao
     * @date 2021/7/22
     * @param token token
     * @return java.lang.Boolean
     **/
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    /**
     * 为用户生成jwt
     * @author liangtao
     * @date 2021/7/22
     * @param userDetails security的用户信息接口
     * @return java.lang.String jwt
     **/
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }


    /**
     * 创建令牌时 -
     * 1. 定义令牌的声明，如颁发者、到期日、主题和 ID
     * 2. 使用 HS512 算法和密钥对 JWT 进行签名。
     * 3. 根据 JWS Compact Serialization 将 JWT 压缩为 URL 安全字符串(头部.载荷.签名)
     * @author liangtao
     * @date 2021/7/22
     * @param claims payload 载荷,包括公有声明，私有声明,标准声明
     * @param subject 主体人,通常标识jwt的用户身份（username/id)
     * @return java.lang.String
     **/
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * token合法性检查
     * @author liangtao
     * @date 2021/7/22
     * @param token token
     * @param userDetails 用户信息
     * @return java.lang.Boolean
     **/
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
