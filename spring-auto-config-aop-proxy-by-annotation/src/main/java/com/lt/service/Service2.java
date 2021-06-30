package com.lt.service;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;

/**
 * @author liangtao
 * @description 2
 * @date 2020年11月23 15:32
 **/
@Service
public class Service2 {
    public void m2() throws InterruptedException {
        int millis = RandomUtil.randomInt(3000, 20000);
        System.out.println("内部耗时： "+millis);
        System.out.println(this.getClass() + ".m2()");
        Thread.sleep(millis);
    }
}
