package com.lt.redis;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lt.redis.model.PersonTest;
import com.lt.redis.repository.RedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class RedisRepositoryTest {

    @Autowired
    RedisRepository redisRepository;
    private static final ObjectMapper om = new ObjectMapper();

    private static final String USER_KEY = "user";
    private static final String HASH_KEY = "user_group";
    private static final String LIST_KEY = "user_list";

    @Test
    void getConnectionFactory() {
        RedisConnectionFactory connectionFactory = redisRepository.getConnectionFactory();
        Assert.isTrue(connectionFactory != null);
    }

    @Test
    void getRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = redisRepository.getRedisTemplate();
        Assert.isTrue(redisTemplate != null);
    }

    @Test
    void testSetExpire() {
        PersonTest zs = PersonTest.builder().name("张三").age(12).addr("安徽").build();
        boolean result = redisRepository.setExpire(USER_KEY, zs, 5000);
        Assert.isTrue(result);
        PersonTest personTest = redisRepository.get(USER_KEY);
        Assert.isTrue(personTest != null && personTest.equals(zs));
        List<String> keys = redisRepository.willExpire(USER_KEY, 5001);
        Assert.isTrue(keys.contains(USER_KEY));
    }


    public List<PersonTest> buildPerson() {
        PersonTest zs = PersonTest.builder().name("张三").age(12).addr("安徽").build();
        PersonTest ls = PersonTest.builder().name("李四").age(19).addr("江苏").build();
        PersonTest ww = PersonTest.builder().name("王五").age(11).addr("南京").build();
        List<PersonTest> personList = Arrays.asList(zs, ls, ww);
        return personList;
    }

    @Test
    void testBatchSet() throws IOException {
        List<PersonTest> personTests = buildPerson();
        boolean result = redisRepository.setExpire(USER_KEY.getBytes(StandardCharsets.UTF_8), om.writeValueAsBytes(personTests), 30);
        Assert.isTrue(result);
        List<PersonTest> redisPersonList = om.readValue(
                redisRepository.get(USER_KEY.getBytes(StandardCharsets.UTF_8))
                , new TypeReference<List<PersonTest>>() {
                });
        Assert.isTrue(personTests.equals(redisPersonList));
        redisRepository.setExpire(new String[]{"1", "2", "3"}, personTests.toArray(), 20);
        List<String> willList = redisRepository.willExpire("", 21);
        Assert.isTrue(willList.contains("1") && willList.contains("2") && willList.contains("3"));
    }

    @Test
    public void hashMapTest() throws IOException {
        testBatchSet();
        Map<String, PersonTest> keysValues = redisRepository.getKeysValues("1");
        keysValues.forEach((k, v) -> {
            System.out.println("k = " + k);
            System.out.println("v = " + v);
        });
    }


    @Test
    public void delTest() {
        PersonTest zs = PersonTest.builder().name("张三").age(12).addr("安徽").build();
        redisRepository.set("del_key",zs);
        long count = redisRepository.deleteByKeys("del_key");
        Assert.isTrue(count==1L);
    }


}
