package com.lt.myRule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author liangtao
 * @Date 2020/6/9
 **/
@Configuration
public class MySelfRule {
    @Resource
    DiscoveryClient discoveryClient;
    @Bean
    public IRule myRule(){
        return new RandomRule();//定义为随机
    }


/*
    private class MyRule extends AbstractLoadBalancerRule {
        AtomicInteger count=new AtomicInteger(0);

        @Override
        public void initWithNiwsConfig(IClientConfig iClientConfig) {
            String clientName = iClientConfig.getClientName();
            Map<String, Object> properties = iClientConfig.getProperties();
            properties.forEach((k,v)->{
                System.out.println("k = " + k);
                System.out.println("v = " + v);
            });
            System.out.println("clientName = " + clientName);

        }

        @Override
        public Server choose(Object o) {
            List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

        }
    }
*/
}
