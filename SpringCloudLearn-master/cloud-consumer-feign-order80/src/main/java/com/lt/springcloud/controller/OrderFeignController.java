package com.lt.springcloud.controller;

import com.lt.springcloud.entities.CommonResult;
import com.lt.springcloud.entities.Payment;
import com.lt.springcloud.service.PaymentFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liangtao
 * @Date 2020/6/10
 **/
@RestController
public class OrderFeignController {
    @Resource
    PaymentFeignService paymentFeignService;

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return paymentFeignService.selectOne(id);
    }
}

