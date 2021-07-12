package com.lt;

import com.lt.dao.UserDao;
import com.lt.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liangtao
 * @description
 * @Date 2021/7/12
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Resource
    UserDao userDao;

    @Test
    public void userTest(){
        User user1 = new User("里斯", 11, "黑龙江");
        userDao.insert(user1);
        User userDb = userDao.getByName("里斯");
        System.out.println(userDb);
        System.out.println("-----------");

        for (User user : userDao.list()) {
            System.out.println(user);
        }
    }
}
