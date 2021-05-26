package com.lt.redis.controller;

import cn.hutool.core.util.RandomUtil;
import com.lt.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.lt.redis.interceptor.ClickCollectInterceptor.ACCESS_INTERFACE_CONSUMER;
import static com.lt.redis.interceptor.ClickCollectInterceptor.ACCESS_INTERFACE_TOTAL;

/**
 * @author liangtao
 * @description 点击测试controller, 用于收集接口的点击
 * @date 2021年05月25 10:50
 **/
@RestController
public class ClickCollectController {

    @Autowired
    RedisRepository redisRepository;

    /**
     * A接口，耗时在0.3秒到2.5秒之间
     *
     * @param
     * @return java.lang.String
     * @author liangtao
     * @date 2021/5/26
     **/
    @GetMapping("clickA")
    public String clickCollectA() throws InterruptedException {
        int min = 300;
        int max = 2500;
        Thread.sleep(RandomUtil.randomInt(min, max));
        return "success";
    }

    @GetMapping("clickB")
    public String clickCollectB() throws InterruptedException {
        int min = 20;
        int max = 500;
        Thread.sleep(RandomUtil.randomInt(min, max));
        return "success";
    }

    @GetMapping("clickC")
    public String clickCollectC() throws InterruptedException {
        int min = 600;
        int max = 4000;
        Thread.sleep(RandomUtil.randomInt(min, max));
        return "success";
    }

    @GetMapping("clickD")
    public String clickCollectD() throws InterruptedException {
        int min = 30;
        int max = 200;
        Thread.sleep(RandomUtil.randomInt(min, max));
        return "success";
    }

    @GetMapping("clickE")
    public String clickCollectE() throws InterruptedException {
        int min = 2000;
        int max = 2500;
        Thread.sleep(RandomUtil.randomInt(min, max));
        return "success";
    }

    @GetMapping("queryClickRank")
    public List<String> queryClickRank() {
        ZSetOperations<String, String> operations = redisRepository.opsForZSet();
        Set<String> routerSet = operations.reverseRange(ACCESS_INTERFACE_TOTAL, 0, -1);
        List<String> result=new ArrayList<>(routerSet.size());
        for (String router : routerSet) {
            int clickTotal = operations.score(ACCESS_INTERFACE_TOTAL, router).intValue();
            Double consumer = operations.score(ACCESS_INTERFACE_CONSUMER, router);
            String str = String.format("%s 接口平均耗时： %s", router, (clickTotal / consumer));
            result.add(str);
        }
        return result;
    }

}
