package com.lt.springcloud.loadbalance;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author liangtao
 * @Date 2020/6/10
 **/
public interface LoadBalancer {
    ServiceInstance instance(List<ServiceInstance> serviceInstances);
}
