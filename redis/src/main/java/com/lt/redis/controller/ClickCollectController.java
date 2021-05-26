package com.lt.redis.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangtao
 * @description 点击测试controller,用于收集接口的点击
 * @date 2021年05月25 10:50
 **/
@RestController
public class ClickCollectController {

    /**
     * A接口，耗时在0.3秒到2.5秒之间
     * @author liangtao
     * @date 2021/5/26
     * @param
     * @return java.lang.String
     **/
    @GetMapping("clickA")
    public String clickCollectA() throws InterruptedException {
        int min=300;
        int max=2500;
        Thread.sleep(RandomUtil.randomInt(min,max));
        return "success";
    }

    @GetMapping("clickB")
    public String clickCollectB() throws InterruptedException {
        int min=20;
        int max=500;
        Thread.sleep(RandomUtil.randomInt(min,max));
        return "success";
    }

    @GetMapping("clickC")
    public String clickCollectC() throws InterruptedException {
        int min=600;
        int max=4000;
        Thread.sleep(RandomUtil.randomInt(min,max));
        return "success";
    }

    @GetMapping("clickD")
    public String clickCollectD() throws InterruptedException {
        int min=30;
        int max=200;
        Thread.sleep(RandomUtil.randomInt(min,max));
        return "success";
    }

    @GetMapping("clickE")
    public String clickCollectE() throws InterruptedException {
        int min=2000;
        int max=2500;
        Thread.sleep(RandomUtil.randomInt(min,max));
        return "success";
    }

//    @GetMapping("queryClickRank")
//    public List<String> queryClickRank(){
//        List<String> zRouterList = RedisClientFactory.buildCommand().zrevrange(INTERFACE_CLICK_NUMBER, 0L, -1L);
//        return zRouterList.stream()
//                .map(router -> router + ":" + RedisClientFactory.buildCommand().zscore(INTERFACE_CLICK_NUMBER, router))
//                .collect(Collectors.toList());
//    }

}
