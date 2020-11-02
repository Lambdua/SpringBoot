package com.lt.redis.config;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.lt.redis.repository.RedisRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;

/**
 *  redis 配置类
 * @author liangtao
 * @date 2020/10/30
 **/
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {RedisAutoConfiguration.class}) )
@ConditionalOnClass({RedisOperations.class, RedisRepository.class})
@EnableConfigurationProperties({RedisProperties.class, CacheManagerProperties.class})
@EnableCaching
@Configuration
public class RedisAutoConfigure {

    @Resource
    private CacheManagerProperties cacheManagerProperties;

    /**
     * 自定义缓存key的生成策略。默认的生成策略是看不懂的(乱码内容)
     * 通过Spring的依赖注入特性进行自定义的配置注入并且此类是一个配置类可以更多程度的自定义配置
     * @author liangtao
     * @date 2020/10/30
     * @return org.springframework.cache.interceptor.KeyGenerator
     **/
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName()).append(":").append(method.getName()).append(":");
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 设置json序列化，这里使用jackson
     *
     * @return org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
     * @author liangtao
     * @date 2020/10/30
     **/
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    /**
     * 配置缓存管理器
     *
     * @param connectionFactory redis连接工厂，这里使用Lettuce连接
     * @return CacheManager
     * @author liangtao
     * @date 2020/10/30
     * @see Primary 注解，用于当项目中又多个CacheManager且都有@Bean注解时，
     * 通过@Autowired注解spring会优先使用带有@Primay注解的Bean,或者使用@Qualifier("Bean名称")来注入指定的Bean
     **/
    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        //设置缓存过期时间为1小时
        RedisCacheConfiguration difConf = getDefConf().entryTtl(Duration.ofHours(1));

        //自定义的缓存过期时间配置
        int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = MapUtil.newHashMap(configSize);
        if (configSize > 0) {
            cacheManagerProperties.getConfigs().forEach(e -> {
                //为每一个cache 设置自己的过期时间
                RedisCacheConfiguration conf = getDefConf().entryTtl(Duration.ofSeconds(e.getSecond()));
                redisCacheConfigurationMap.put(e.getKey(), conf);
            });
        }
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(difConf)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }


    /**
     * 自定义RedisTemplate,这里使用{@link StringRedisTemplate},存储使用json转换
     * 此种序列化方式结果清晰、容易阅读、存储字节少、速度快，所以推荐更换
     * @param connectFactory redis连接工厂
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory connectFactory,Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(connectFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setDefaultSerializer(stringSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 设置默认redis cache配置 ：不允许缓存空值，
     * 设置CacheManager的Value序列化方式为JdkSerializationRedisSerializer, 但其实RedisCacheConfiguration默认就是使用
     * StringRedisSerializer序列化key，
     * JdkSerializationRedisSerializer序列化value,
     * @return org.springframework.data.redis.cache.RedisCacheConfiguration
     * @author liangtao
     * @date 2020/10/30
     **/
    private RedisCacheConfiguration getDefConf() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"));
    }

}
