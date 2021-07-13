package com.lt.service;

import com.lt.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liangtao
 * @description 业务service
 * @Date 2021/7/12
 **/
public interface UserAddService {

    boolean addUser(User user);


    @Transactional(propagation = Propagation.REQUIRED)
    void transaction();
}
