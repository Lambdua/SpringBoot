package com.lt.springcloud.service.impl;

import com.lt.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.lt.springcloud.dao.PaymentDao;
import com.lt.springcloud.service.PaymentService;

import javax.annotation.Resource;

/**
 * @author liangtao
 * @Date 2020/6/2
 **/
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;
    @Override
    public Payment queryById(Long id) {
        return paymentDao.queryById(id);
    }

    @Override
    public int create(Payment entity) {
        return paymentDao.insert(entity);
    }
}
