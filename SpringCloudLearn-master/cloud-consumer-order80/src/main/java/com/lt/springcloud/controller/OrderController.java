package com.lt.springcloud.controller;

import com.lt.springcloud.entities.CommonResult;
import com.lt.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author liangtao
 * @Date 2020/6/2
 **/
@RestController
@RequestMapping("consumer/payment")
@Slf4j
public class OrderController {
//    private static final String PAYMENT_URL="http://localhost:8001/payment";
    private static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE/payment";
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("create")
    public CommonResult<Payment> create(@RequestBody Payment payment){
        log.info(payment.toString());
        return restTemplate.postForObject(PAYMENT_URL+"/create",payment,CommonResult.class);
    }

    @GetMapping("selectOne/{id}")
    public CommonResult<Payment> selectOne(@PathVariable("id")Long id){
        log.info(String.valueOf(id));
        return restTemplate.getForObject(PAYMENT_URL+"/selectOne/"+id,CommonResult.class);
    }
}
