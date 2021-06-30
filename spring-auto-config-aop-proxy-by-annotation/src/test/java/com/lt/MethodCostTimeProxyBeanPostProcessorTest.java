package com.lt;

import com.lt.service.Service1;
import com.lt.service.Service2;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringJUnit4ClassRunner.class)
class MethodCostTimeProxyBeanPostProcessorTest {

    @Autowired
    Service1 service1;
    @Autowired
    Service2 service2;


    @Test
    public void test() throws InterruptedException {
        service1.m1();
        service2.m2();
    }

}
