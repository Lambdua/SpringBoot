package com.lt.service;

import com.lt.model.User;

import java.util.List;

/**
 * @author liangtao
 * @description 模拟业务Service
 * @date 2021年07月13 08:41
 **/
public interface UserModifyService {
    User getById(int id);

    void delByName(String name);

    void updateById(User user);

    List<User> list();
}
