package com.lt.springcloud.controller;

import com.lt.springcloud.entities.CommonResult;
import com.lt.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.lt.springcloud.service.PaymentService;

import javax.annotation.Resource;

/**
 * (Payment)表控制层
 *
 * @author liangtao
 * @since 2020-06-02 16:08:24
 */
@RestController
@RequestMapping("payment")
@Slf4j
public class PaymentController {
    /**
     * 服务对象
     */
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public CommonResult<Payment> selectOne(@PathVariable("id") Long id) {
        Payment payment = paymentService.queryById(id);
        if (payment == null)
            return new CommonResult<Payment>(444, "没有对应记录");
        else
            return new CommonResult<>(200, "获取成功,对应端口号"+serverPort, payment);
    }

    @PostMapping("create")
    public CommonResult<Integer> create(@RequestBody Payment payment) {
        log.info(payment.toString());
        int result = paymentService.create(payment);
        log.info("插入结果" + result);
        if (result > 0) {
            return new CommonResult<Integer>(200, "插入成功,对应端口号"+serverPort, result);
        } else {
            return new CommonResult<Integer>(444, "插入失败,对应端口号："+serverPort);
        }
    }

}
