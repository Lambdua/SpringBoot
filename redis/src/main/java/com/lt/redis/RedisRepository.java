package com.lt.redis;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author liangtao
 * @Date 2020/7/6
 **/
@Component
@Slf4j
public class RedisRepository implements InitializingBean {
    /**
     * 默认编码
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Spring Redis Template
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * key序列化
     */
    private RedisSerializer strSerializer;

    /**
     * value 序列化
     */
    private final JdkSerializationRedisSerializer objSerializer = new JdkSerializationRedisSerializer();
    private RedisSerializer valueSerializeer;



    /**
     * 获取链接工厂
     */
    public RedisConnectionFactory getConnectionFactory() {
        return this.redisTemplate.getConnectionFactory();
    }

    /**
     * 获取 RedisTemplate对象
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
    /**
     * hash操作
     *
     * @return
     */
    public HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * redis操作List
     */
    public ListOperations<String,Object> opsForList(){return redisTemplate.opsForList();}




    /**
     * 清空DB
     *
     * @param node redis 节点
     */
    public void flushDB(RedisClusterNode node) {
        redisTemplate.opsForCluster().flushDb(node);
    }


    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    public boolean setExpire(final byte[] key, final byte[] value, final long time) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            boolean b = connection.setEx(key, time, value);
            if (b) {
                log.debug("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为{}秒", key, time);
                return true;
            } else {
                log.error("[redisTemplate redis]放入 缓存失败{}", time);
                return false;
            }
        });
    }

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    public boolean setExpire(final String key, final Object value, final long time) {
        return setExpire(strSerializer.serialize(key), valueSerializeer.serialize(value), time);
    }

    /**
     * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
     *
     * @param keys   redis主键数组
     * @param values 值数组
     * @param time   过期时间(单位秒)
     */
    public boolean setExpire(final String[] keys, final Object[] values, final long time) {
        if (keys.length != values.length) {
            log.error("keys与values 不匹配");
            return false;
        }
        return redisTemplate.execute((RedisCallback<Boolean>) connectio -> {
            for (int i = 0; i < keys.length; i++) {
                if (connectio.setEx(strSerializer.serialize(keys[i]), time, valueSerializeer.serialize(values[i]))) {
                    log.debug("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为{}秒", keys[i], time);
                } else {
                    log.error("[redisTemplate redis]放入 {}:缓存失败{},终止方法执行", keys[i], time);
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
     *
     * @param keys   the keys
     * @param values the values
     */
    public boolean set(final String[] keys, final Object[] values) {
        if (keys.length != values.length) {
            log.error("keys与values 不匹配");
            return false;
        }
        return redisTemplate.execute((RedisCallback<Boolean>) connectio -> {
            for (int i = 0; i < keys.length; i++) {
                if (connectio.set(strSerializer.serialize(keys[i]), valueSerializeer.serialize(values[i]))) {
                    log.debug("[redisTemplate redis]放入 缓存  url:{} ", keys[i]);
                } else {
                    log.error("[redisTemplate redis]放入 缓存 url:失败{},终止方法执行", keys[i]);
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * 添加到缓存
     *
     * @param key   the key
     * @param value the value
     */
    public boolean set(final String key, final Object value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            if (connection.set(serializer.serialize(key), valueSerializeer.serialize(value))) {
                log.debug("[redisTemplate redis]放入 缓存  url:{}", key);
                return true;
            } else {
                log.error("[redisTemplate redis]放入 缓存 url:失败{},终止方法执行", key);
                return false;
            }
        });
    }

    /**
     * 查询在这个时间段内即将过期的key
     *
     * @param key  the key
     * @param time the time
     * @return the list
     */
    public List<String> willExpire(final String key, final long time) {
        ArrayList<String> keyList = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            keys.forEach(item -> {
                Long ttl = connection.ttl(strSerializer.serialize(key));
                if (ttl > 0 && ttl < time) {
                    keyList.add(item);
                }
            });
            return keyList;
        });
        return keyList;
    }

    /**
     * 查询在以keyPatten的所有  key
     *
     * @param keyPatten the key patten
     * @return the set
     */
    public Set<String> keys(final String keyPatten) {
        return redisTemplate.keys(keyPatten + "*");
       /* return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<byte[]> keys = connection.keys(strSerializer.serialize(keyPatten + "*"));
            return keys.stream().map(keyByte->(String)strSerializer.deserialize(keyByte)).collect(Collectors.toSet());
        });*/
    }

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the byte [ ]
     */
    public byte[] get(final byte[] key) {
        byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
        log.debug("[redisTemplate redis]取出 缓存  url:{} ", key);
        return result;
    }

    public Object get(final String key) {
        Object obj = redisTemplate.execute((RedisCallback<Object>) connect -> {
            byte[] bytes = connect.get(strSerializer.serialize(key));
            return valueSerializeer.deserialize(bytes);
        });
        log.debug("[redisTemplate redis]取出 缓存  url:{} ", key);
        return obj;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strSerializer = redisTemplate.getStringSerializer();
        valueSerializeer = redisTemplate.getValueSerializer();
    }

    /**
     * 根据key获取对象
     *
     * @param keyPatten the key patten
     * @return the keys values
     */
    public Map<String, Object> getKeysValues(final String keyPatten) {
        log.debug("[redisTemplate redis]  getValues()  patten={} ", keyPatten);
        return redisTemplate.execute((RedisCallback<Map<String, Object>>) connection -> {
            Map<String, Object> maps = new HashMap<>(16);
            Set<String> keys = redisTemplate.keys(keyPatten + "*");
            if (ArrayUtil.isNotEmpty(keys)) {
                for (String key : keys) {
                    byte[] bKeys = strSerializer.serialize(key);
                    byte[] bValues = connection.get(bKeys);
                    Object value = valueSerializeer.deserialize(bValues);
                    maps.put(key, value);
                }
            }
            return maps;
        });
    }

    public long deleteByKeys(String...keys){
         return redisTemplate.delete(CollectionUtil.toList(keys));
    }

    /**
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public void putHashValue(String key, String hashKey, Object hashValue) {
        log.debug("[redisTemplate redis]  putHashValue()  key={},hashKey={},hashValue={} ", key, hashKey, hashValue);
        opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    public Object getHashValue(String key, String hashKey) {
        log.debug("[redisTemplate redis]  getHashValues()  key={},hashKey={}", key, hashKey);
        return opsForHash().get(key, hashKey);
    }

    public Set<String> getHashKeysByKey(String key){
        return opsForHash().keys(key);
    }

    /**
     * 根据hashkey删除
     */
    public Long deleteHashValus(String key, String... hashKeys) {
        return opsForHash().delete(key, hashKeys);
    }

    /**
     * key只匹配map
     *
     * @param key the key
     * @return the hash value
     */
    public Map<String, Object> getHashValues(String key) {
        return opsForHash().entries(key);
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
     * 删除keys
     */
    public long deleteKey(String... keys) {
        return redisTemplate.execute((RedisCallback<Long>) connect -> {
            Long result = 0L;
            for (int i = 0; i < keys.length; i++) {
                result += connect.del(strSerializer.serialize(keys[i]));
            }
            return result;
        });
    }

    /**
     * 将一个或者多个值插入到list的表头
     */
    public long leftPush(String key,Object...values){
        return opsForList().leftPushAll(key,values);
    }

    /**
     * 对应leftPush
     */
    public Object leftPop(String key){
        return opsForList().leftPop(key);
    }
    public long rightPosh(String key,Object...value){
        return opsForList().rightPush(key,value);
    }
    public Object rightPop(String key){
        return opsForList().rightPop(key);
    }

    /**
     * 返回size
     */
    public long listSize(String key){
        return opsForList().size(key);
    }

    /**
     *
     * @param key
     * @param value
     * @param count 删除条数
     * @return
     */
    public long remove(String key,Object value,long count){
        return opsForList().remove(key,count,value);
    }

    public void listSet(String key,Object value,long index){
        opsForList().set(key,index,value);
    }

    /**
     *
     * @param key
     * @param start
     * @param end -1查询全部
     */
    public List<Object> getList(String key,int start,int end){
        return opsForList().range(key,start,end);
    }

    /**
     * 根据index,获取list中的元素
     * @param key
     * @param index
     * @return
     */
    public Object getListItem(String key,long index){
        return opsForList().index(key,index);
    }
}
