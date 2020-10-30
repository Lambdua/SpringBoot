package com.lt.redis;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class RedisRepositoryTest {

    @Autowired
    RedisRepository redisRepository;

    private static final String USER_KEY = "user";
    private static final String HASH_KEY = "user_group";

    @Test
    void getConnectionFactory() {
    }

    @Test
    void getRedisTemplate() {
    }

    @Test
    void opsForHash() {
    }

    @Test
    void opsForList() {
    }

    @Test
    void flushDB() {
    }

    @Test
    void setExpire() {
    }

    @Test
    void testSetExpire() {
    }

    @Test
    void testSetExpire1() {
    }

    @Test
    void set() {
    }

    @Test
    void testSet() {
    }

    @Test
    void willExpire() {
    }

    @Test
    void keys() {
    }

    @Test
    void get() {
    }

    @Test
    void testGet() {
    }

    @Test
    void afterPropertiesSet() {
    }

    @Test
    void getKeysValues() {
    }

    @Test
    void deleteByKeys() {
    }

    @Test
    void putHashValue() {
    }

    @Test
    void getHashValue() {
    }

    @Test
    void getHashKeysByKey() {
    }

    @Test
    void deleteHashValus() {
    }

    @Test
    void getHashValues() {
    }

    @Test
    void dbSize() {
    }

    @Test
    void exists() {
    }

    @Test
    void deleteKey() {
    }

    @Test
    void leftPush() {
    }

    @Test
    void leftPop() {
    }

    @Test
    void rightPosh() {
    }

    @Test
    void rightPop() {
    }

    @Test
    void listSize() {
    }

    @Test
    void remove() {
    }

    @Test
    void listSet() {
    }

    @Test
    void getList() {
    }

    @Test
    void getListItem() {
    }
}
