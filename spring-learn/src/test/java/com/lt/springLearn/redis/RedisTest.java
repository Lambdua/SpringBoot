package com.lt.springLearn.redis;

import cn.hutool.core.util.RandomUtil;
import com.lt.springLearn.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @author liangtao
 * @Date 2020/7/6
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    RedisRepository redisRepository;

    private static final String USER_KEY = "user";
    private static final String HASH_KEY = "user_group";

    @Test
    public void kvTest(){
        setTest();
        getTest();
        deleteTest();
    }
    @Test
    public void hashTest(){
        hashPushTest();
        hashGetTest();
        hashKeysTest();
        hashDeleteTest();
    }

    /*
    keyValueDemo
     */
    public User createUser() {
        return new User(RandomUtil.randomString(4), LocalDateTime.now(), RandomUtil.randomLong(100));
    }

    public void setTest() {
        boolean set = redisRepository.set(USER_KEY, createUser());
        Assert.assertTrue(set);
    }

    public void getTest() {
        User user = (User) redisRepository.get(USER_KEY);
        Assert.assertNotNull(user);
    }

    public void deleteTest() {
        long count = redisRepository.deleteByKeys(USER_KEY);
        Assert.assertEquals(1L, count);
    }

    /*
    hashDemo
     */
    public void hashPushTest() {
        for (int i = 0; i < 20; i++) {
            redisRepository.putHashValue(HASH_KEY, USER_KEY + "_" + i, createUser());
        }
        Set<String> keys = redisRepository.keys(USER_KEY);
        Assert.assertEquals(1,keys.size());
    }

    public void hashGetTest(){
        Map<String, Object> values = redisRepository.getHashValues(HASH_KEY);
        int size = values.values().size();
        Assert.assertEquals(20,size);
    }

    public void hashKeysTest(){
        Set<String> hashKeys = redisRepository.getHashKeysByKey(HASH_KEY);
        Map<String, Object> hashValues = redisRepository.getHashValues(HASH_KEY);
        Assert.assertEquals(hashValues.size(),hashKeys.size());
    }

    public void hashDeleteTest(){
        Set<String> hashKeys = redisRepository.getHashKeysByKey(HASH_KEY);
        Long deleteCounts = redisRepository.deleteHashValus(HASH_KEY, hashKeys.toArray(new String[hashKeys.size()]));
        Assert.assertEquals(deleteCounts,Long.valueOf(hashKeys.size()));
    }




}
