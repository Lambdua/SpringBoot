package com.lt.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


/**
 * redis cache自定义配置Properties
 * @author liangtao
 * @date 2020/10/30
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "agile.cache-manager")
public class CacheManagerProperties {
    private List<CacheConfig> configs;

    @Setter
    @Getter
    public static class CacheConfig {
        /**
         * cache key
         */
        private String key;
        /**
         * 过期时间，sec
         */
        private long second = 60;
    }
}
