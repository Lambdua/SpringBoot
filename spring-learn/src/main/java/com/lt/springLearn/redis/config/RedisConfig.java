package com.lt.springLearn.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liangtao
 * @Date 2020/7/6
 * redisTemplate的配置类
 **/
//@Configuration
public class RedisConfig extends JCacheConfigurerSupport {
    private final String CACHE_SET1 = "my-redis-cache1";
    private final String CACHE_SET2 = "my-redis-cache2";

    /**
     * 配置cacheManager
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        //生成一个默认配置，在对该对象进行配置，实现自定义redis缓存需求
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        //每次定义设置，方法都会有一个返回值，所以需要接受返回值，重写cacheConfiguration
        cacheConfiguration = cacheConfiguration
                //设置默认过期时间
                .entryTtl(Duration.ofMinutes(2L))
                //不缓存空值
                .disableCachingNullValues();
        //设置一个初始化的缓存空间set集合
        Set<String> cacheSet = new HashSet<>();
        cacheSet.add(CACHE_SET1);
        cacheSet.add(CACHE_SET2);

        //对每一个缓存空间配置不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put(CACHE_SET1, cacheConfiguration);
        configMap.put(CACHE_SET2, cacheConfiguration.entryTtl(Duration.ofSeconds(30L)));

        // 使用自定义的缓存配置初始化一个cacheManager
        return RedisCacheManager
                .builder(factory)
                //初始化缓存名
                .initialCacheNames(cacheSet)
                //在初始化对应配置，注意先后顺序
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //设置序列化使用类
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setStringSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(new RedisObjectSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
