package com.lt.model;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liangtao
 * @date 2020/10/28
 **/
@Data
@AllArgsConstructor
public class UserDto {
    private static final String baseStr = "放假啊空间高建刚南方根据客户维两套公开赶紧发给我度过哦感觉几个国家奋斗过护今天";

    private String id;
    private String username;
    private String password;
    private String mobile;

    public static List<UserDto> createList(int size) {
        List<UserDto> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(new UserDto(IdUtil.fastUUID(), RandomUtil.randomString(baseStr, 3), "123456", RandomUtil.randomNumbers(15)));
        }
        return list;
    }
}
