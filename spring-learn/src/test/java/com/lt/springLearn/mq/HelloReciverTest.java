package com.lt.springLearn.mq;

import com.lt.springLearn.mq.simple.SimpleSender;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.jupiter.api.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class HelloReciverTest {

    @Autowired
    private SimpleSender helloSender;


    @Test
    public void hello(){
        for (int i = 0; i < 100; i++) {
            helloSender.send(String.valueOf(i));
        }
    }
}
