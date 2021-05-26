package com.lt.redis.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author liangtao
 * @description 点击收集测试工具类
 * @date 2021年05月26 10:29
 **/
public class ClickCollectRequestTestUtil {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int j = 1;
        while (true) {
            System.out.println("开始第" + (j++) + "轮请求");
            String url = "http://localhost:9991/";
            String[] routers = new String[]{"clickA", "clickB", "clickC", "clickD", "clickE"};
            //C30% A10% B10% D40% E10%
            int[] index = new int[]{0, 1, 2, 2, 2, 3, 3, 3, 3, 4};
            Runnable doGetRunnable = () -> {
                try {
                    Thread.sleep(100);
                    //每一个请求发30个
                    for (int i = 0; i < 300; i++) {
                        HttpRequest.get(url + routers[index[RandomUtil.randomInt(0, 10)]]).execute();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            List<CompletableFuture<Void>> futureList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                futureList.add(doSomethingOne(doGetRunnable));
            }
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get();
        }
    }

    public static CompletableFuture<Void> doSomethingOne(Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }
}
