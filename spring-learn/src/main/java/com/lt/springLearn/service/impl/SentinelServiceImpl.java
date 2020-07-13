package com.lt.springLearn.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lt.springLearn.service.SentinelService;
import org.springframework.stereotype.Service;

/**
 * @author liangtao
 * @Date 2020/7/9
 **/
@Service
public class SentinelServiceImpl implements SentinelService {
    @Override
    @SentinelResource(value = "message")
    public String sayHello(String id) {
        return "hello :"+id;
    }
}
