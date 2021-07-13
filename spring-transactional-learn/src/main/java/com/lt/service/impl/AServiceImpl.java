package com.lt.service.impl;

import com.lt.dao.UserDao;
import com.lt.model.User;
import com.lt.service.BusinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liangtao
 * @description
 * @Date 2021/7/12
 **/
@Service
public class AServiceImpl implements BusinessService {
    @Resource
    UserDao userDao;

    @Override
    public String doSomething() {
        List<User> list = userDao.list();
        list.forEach(user-> System.out.println(user));
        return String.valueOf(list.size());
    }
}
