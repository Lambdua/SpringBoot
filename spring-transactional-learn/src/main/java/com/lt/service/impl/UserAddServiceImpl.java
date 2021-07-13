package com.lt.service.impl;

import com.lt.dao.UserDao;
import com.lt.model.User;
import com.lt.service.UserAddService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liangtao
 * @description
 * @Date 2021/7/12
 **/
@Service
public class UserAddServiceImpl implements UserAddService {
    @Resource
    UserDao userDao;

    @Override
    public boolean addUser(User user) {
        userDao.insert(user);
        return true;
    }
}
