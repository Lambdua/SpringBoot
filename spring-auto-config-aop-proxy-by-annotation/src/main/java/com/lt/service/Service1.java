package com.lt.service;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;

/**
 * @author liangtao
 * @description 1
 * @date 2020年11月23 15:31
 **/
@Service
public class Service1 {
    public void m1() throws InterruptedException {
        int millis = RandomUtil.randomInt(3000, 20000);
        System.out.println("内部耗时： "+millis);
        Thread.sleep(millis);
        System.out.println(this.getClass() + ".m1()");
    }
}
