package com.lt.service;

import com.lt.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liangtao
 * @description 模拟业务Service
 * @date 2021年07月13 08:41
 **/
public interface UserModifyService {
    User getById(int id);

    @Transactional(propagation = Propagation.REQUIRED)
    void delByName(String name);


    void updateById(User user);

//    @Transactional(propagation = Propagation.REQUIRED)
//    @Transactional(propagation = Propagation.SUPPORTS)

    @Transactional(propagation = Propagation.REQUIRED)
    void updateByName(User user);

    List<User> list();
}
