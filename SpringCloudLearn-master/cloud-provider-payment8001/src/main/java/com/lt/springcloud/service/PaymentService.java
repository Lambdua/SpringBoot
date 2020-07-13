package com.lt.springcloud.service;

import com.lt.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * (Payment)表服务接口
 *
 * @author liangtao
 * @since 2020-06-02 15:56:05
 */
public interface PaymentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Payment queryById(@Param("id") Long id);

    /**
     * 保存
     * @return
     */
    int create(Payment entity);

}
