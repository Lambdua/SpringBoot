package com.lt.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.lt.model.AuthenticationRequest;
import com.lt.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 认证服务,不使用接口+实现类的方式，因为觉得不是很有必要
 *
 * @author 梁先生
 * @Date 2020/10/28
 **/
@Service
public class AuthenticationService {
    /**
     * 用户信息
     */
    private static final Map<String, UserDto> USER_MAP = MapUtil.newHashMap(2);

    static {
        USER_MAP.put("zhangsan", new UserDto("1010", "zhangsan", "123", "133443"));
        USER_MAP.put("lisi", new UserDto("1011", "lisi", "456", "144553"));
    }

    /**
     * 用户认证，校验用户信息身份是否合法,这里模拟了两个用户，来认证
     *
     * @param authenticationRequest 用户认证请求 包含用户名和密码
     * @return com.lt.model.UserDto 认证成功的信息
     * @author liangtao
     * @date 2020/10/28
     **/
    public UserDto authentication(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null
                || StrUtil.isEmpty(authenticationRequest.getUsername())
                || StrUtil.isEmpty(authenticationRequest.getPassword())) {
            throw new RuntimeException("认证参数为空");
        }
        //根据用户名去查询数据库,这里模拟
        UserDto userDto = getUserDtoByUserName(authenticationRequest.getUsername());
        if (userDto == null || !authenticationRequest.getPassword().equals(userDto.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        return userDto;
    }

    /**
     * 根据username获取用户DTO
     *
     * @param userName 用户名
     * @return com.lt.model.UserDto 用户信息
     * @author liangtao
     * @date 2020/10/28
     **/
    private UserDto getUserDtoByUserName(String userName) {
        return USER_MAP.get(userName);
    }
}
