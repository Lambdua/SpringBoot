package com.lt.springcloud.loadbalance.impl;

import com.lt.springcloud.loadbalance.LoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liangtao
 * @Date 2020/6/10
 **/
@Component
public class SimplyLoadBalancer implements LoadBalancer {
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {
        return serviceInstances.get(getAndIncrement()%serviceInstances.size());
    }

    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = count.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!count.compareAndSet(current,next));
        System.out.println("next = " + next);
        return next;
    }

}
