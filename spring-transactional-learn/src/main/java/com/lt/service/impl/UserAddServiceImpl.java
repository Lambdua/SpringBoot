package com.lt.service.impl;

import com.lt.dao.UserDao;
import com.lt.model.User;
import com.lt.service.UserAddService;
import com.lt.service.UserModifyService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserModifyService userModifyService;

    @Override
    public boolean addUser(User user) {
        userDao.insert(user);
        return true;
    }

    @Override
    public void transaction() {
        User user = new User("required", 11, "测试");
        System.out.println("插入数据库");
        addUser(user);
        System.out.println("更新数据库");
        user.setAddr("更新操作，required事务传播行为");
        userModifyService.updateByName(user);
        System.out.println("---");
    }
}
