package com.lt.redis.repository;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis操作类
 * 这里默认使用的是json序列化values
 *
 * @author liangtao
 * @date 2020/7/6
 **/
@Component
@Slf4j
public class RedisRepository implements InitializingBean {

    /**
     * Spring Redis Template
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * key序列化
     */
    private RedisSerializer<String> strSerializer;
    private RedisSerializer<Object> valueSerializer;


    @Override
    public void afterPropertiesSet() {
        strSerializer = redisTemplate.getStringSerializer();
        valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
    }


    /**
     * 获取链接工厂
     */
    public RedisConnectionFactory getConnectionFactory() {
        return redisTemplate.getConnectionFactory();
    }

    /**
     * 获取 RedisTemplate对象
     */
    public  RedisTemplate<String, String> getRedisTemplate() {
        return  redisTemplate;
    }

    /**
     * hash操作
     *
     * @return
     */
    public <V> HashOperations<String, String, V> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * redis操作List
     */
    public <V> ListOperations<String, V> opsForList() {
        return (ListOperations<String, V>) redisTemplate.opsForList();
    }


    public <V> ZSetOperations<String, V> opsForZSet(){
        return (ZSetOperations<String, V>) redisTemplate.opsForZSet();
    }

    /**
     * 清空DB
     *
     * @param node redis 节点
     */
    public void flushDb(RedisClusterNode node) {
        redisTemplate.opsForCluster().flushDb(node);
    }


    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值，必须为json字符转换的字节数组
     * @param time  过期时间(单位秒)
     */
    public boolean setExpire(final byte[] key, final byte[] value, final long time) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.setEx(key, time, value));
    }

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    public <V> boolean setExpire(final String key, final V value, final long time) {
        return setExpire(strSerializer.serialize(key), valueSerializer.serialize(value), time);
    }

    /**
     * 一次性添加数组到指定过期时间的 缓存，不用多次连接，节省开销
     *
     * @param keys   redis主键数组
     * @param values 值,必须为json字符转换的字节数组
     * @param time   过期时间(单位秒)
     */
    public  <T> boolean setExpire(final String[] keys, final T[] values, final long time) {
        if (keys.length != values.length) {
            log.error("keys与values 不匹配");
            return false;
        }
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            boolean result = true;
            for (int i = 0; i < keys.length; i++) {
                result = connection.setEx(strSerializer.serialize(keys[i]), time, valueSerializer.serialize(values[i]));
                if (!result) {
                    log.error("[redisTemplate redis]放入 {}:缓存失败{},终止方法执行", keys[i], values[i]);
                    return result;
                }
            }
            return result;
        });
    }

    /**
     * 一次性添加数组到 ,没有过期时间,不用多次连接，节省开销
     *
     * @param keys   the keys
     * @param values the values,必须为json字符转换的字节数组
     */
    public <V> boolean set(final String[] keys, final V[] values) {
        if (keys.length != values.length) {
            log.error("keys与values 不匹配");
            return false;
        }
        return redisTemplate.execute((RedisCallback<Boolean>) connectio -> {
            boolean result = true;
            for (int i = 0; i < keys.length; i++) {
                result = connectio.set(strSerializer.serialize(keys[i]), valueSerializer.serialize(values[i]));
                if (!result) {
                    log.error("[redisTemplate redis]放入 缓存 url:失败{},终止方法执行", keys[i]);
                    return result;
                }
            }
            return result;
        });
    }

    /**
     * 添加到缓存
     *
     * @param key   the key
     * @param value the value,必须为json字符转换的字节数组
     */
    public <V> boolean set(final String key, final V value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set(strSerializer.serialize(key), valueSerializer.serialize(value)));
    }

    /**
     * 查询在这个时间段内即将过期的key
     *
     * @param key  the key
     * @param time the time 秒
     * @return 即将过期的key集合
     */
    public List<String> willExpire(final String key, final long time) {
        Set<String> keys = keys(key);
        return redisTemplate.execute((RedisCallback<List<String>>) connection ->
                keys.stream().filter(item -> {
                    Long ttl = connection.ttl(strSerializer.serialize(item));
                    return ttl > 0 && ttl < time;
                }).collect(Collectors.toList())
        );
    }

    /**
     * 查询在以keyPatten的所有  key
     *
     * @param keyPatten the key patten
     * @return the set
     */
    public Set<String> keys(final String keyPatten) {
        return redisTemplate.keys(keyPatten + "*");
    }

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the byte [ ]
     */
    public byte[] get(final byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
    }

    /**
     * 根据key获取对象
     *
     * @param key
     * @return T  指定的类型
     * @author liangtao
     * @date 2020/11/2
     **/
    public <T> T get(final String key) {
        return redisTemplate.execute((RedisCallback<T>) connect ->
                (T) valueSerializer.deserialize(connect.get(strSerializer.serialize(key))));
    }


    /**
     * 根据key*获取对象
     *
     * @param keyPatten key模式
     * @return key, value键值对
     */
    public <T> Map<String, T> getKeysValues(final String keyPatten) {
        Set<String> keys = keys(keyPatten);
        return keys.stream().collect(Collectors.toMap(key -> key, key->this.get(key)));
    }


    /**
     * 根据key删除
     *
     * @param keys key数组
     * @return long 删除的key数量，在管道中使用时为kong
     * @author liangtao
     * @date 2020/11/2
     **/
    public long deleteByKeys(String... keys) {
        return redisTemplate.delete(CollUtil.toList(keys));
    }

    /**
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public <V> void putHashValue(String key, String hashKey, V hashValue) {
        opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    public <V> V getHashValue(String key, String hashKey) {
        return (V) opsForHash().get(key, hashKey);
    }

    /**
     * 获取hash keys集合，根据hashKey
     *
     * @param key hash对象key
     * @return java.util.Set<java.lang.String>
     * @author liangtao
     * @date 2020/11/2
     **/
    public Set<String> getHashKeysByKey(String key) {
        return opsForHash().keys(key);
    }

    /**
     * @param key      hash name
     * @param hashKeys hashKeys
     * @return java.lang.Long  删除数量
     * @author liangtao
     * @date 2020/11/2
     **/
    public Long deleteHashValues(String key, Object... hashKeys) {
        return opsForHash().delete(key, hashKeys);
    }

    /**
     * 根据hash nanme获取hash对象
     *
     * @param key hahs name
     * @return the hash 对象
     */
    public <V> Map<String, V> getHashValues(String key) {
        return (Map<String, V>) opsForHash().entries(key);
    }

    /**
     * 集合数量
     */
    public Long dbSize() {
        return redisTemplate.execute(RedisServerCommands::dbSize);
    }

    /**
     * 查看是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(strSerializer.serialize(key)));
    }


    /**
     * 将一个或者多个值插入到list的表头
     *
     * @param key    list的key
     * @param values 插入的value数组
     * @return long 插入的成功数
     * @author liangtao
     * @date 2020/11/2
     **/
    public <V> long leftPush(String key, V... values) {
        return opsForList().leftPushAll(key, values);
    }

    /**
     * 对应leftPush
     */
    public <V> V leftPop(String key) {
        return (V) opsForList().leftPop(key);
    }

    public <V> long rightPush(String key, V... value) {
        return opsForList().rightPush(key, value);
    }

    public <V> V rightPop(String key) {
        return (V) opsForList().rightPop(key);
    }

    /**
     * 返回size
     */
    public long listSize(String key) {
        return opsForList().size(key);
    }

    /**
     * 删除集合指定的条数
     *
     * @param key
     * @param value
     * @param count 删除条数
     * @return
     */
    public <V> long remove(String key, V value, long count) {
        return opsForList().remove(key, count, value);
    }

    /**
     * 指定index,设置集合list的value
     *
     * @param key   list key
     * @param value 插入的值
     * @param index 插入索引
     * @return void
     * @author liangtao
     * @date 2020/11/2
     **/
    public <V> void listSet(String key, V value, long index) {
        opsForList().set(key, index, value);
    }

    /**
     * @param key
     * @param start
     * @param end   -1查询全部
     */
    public <V> List<V> getList(String key, int start, int end) {
        return (List<V>) opsForList().range(key, start, end);
    }

    /**
     * 根据index,获取list中的元素
     *
     * @param key   key
     * @param index 索引位置
     * @return
     */
    public <V> V getListItem(String key, long index) {
        return (V) opsForList().index(key, index);
    }
}
