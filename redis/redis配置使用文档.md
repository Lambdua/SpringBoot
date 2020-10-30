# springboot 集成redis
## 简介
spring boot框架中已经集成了redis，在1.x.x的版本时默认使用的jedis客户端，现在是2.x.x版本默认使用的lettuce客户端，两种客户端的区别如下：
```shell
# Jedis和Lettuce都是Redis Client

# Jedis 是直连模式，在多个线程间共享一个 Jedis 实例时是线程不安全的，
# 如果想要在多线程环境下使用 Jedis，需要使用连接池，
# 每个线程都去拿自己的 Jedis 实例，当连接数量增多时，物理连接成本就较高了。
# Lettuce的连接是基于Netty的，连接实例可以在多个线程间共享，
# 所以，一个多线程的应用可以使用同一个连接实例，而不用担心并发线程的数量。
# 当然这个也是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。

# 通过异步的方式可以让我们更好的利用系统资源，而不用浪费线程等待网络或磁盘I/O。
# Lettuce 是基于 netty 的，netty 是一个多线程、事件驱动的 I/O 框架，
# 所以 Lettuce 可以帮助我们充分利用异步的优势。
```
故使用lettuce来进行连接
## 整合
###　导入依赖
```xml
 <!-- spring boot redis 缓存引入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>2.3.0.RELEASE</version>
        </dependency>
        <!-- lettuce pool 缓存连接池 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.8.0</version>
        </dependency>
        <!--对象序列化存储-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>
```
### 配置文件
```yml
spring:
  application:
    name: redis-learn
  redis:
    port: 6379
    host: 172.168.10.223
    password: agile12345
    lettuce:
      pool:
        #连接池最大连接数（使用负值表示没有限制）
        max-active: 8
        #连接池中的最大空闲连接
        max-idle: 8
        #连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1
        #连接池中的最小空闲连接
        min-idle: 0
    #连接超时时间（毫秒）
    timeout: 30000
```
### 编写配置类
编写缓存配置类CacheConfig用于调优缓存默认配置
spring-data-redis中序列化类有以下几个：

- GenericToStringSerializer：可以将任何对象泛化为字符创并序列化
- Jackson2JsonRedisSerializer：序列化Object对象为json字符创（与JacksonJsonRedisSerializer相同）
- JdkSerializationRedisSerializer：序列化java对象
- StringRedisSerializer：简单的字符串序列化

**JdkSerializationRedisSerializer序列化**

被序列化对象必须实现Serializable接口，被序列化除属性内容还有其他内容，长度长且不易阅读
存储内容如下：
```
"\xac\xed\x00\x05sr\x00!com.oreilly.springdata.redis.User\xb1\x1c \n\xcd\xed%\xd8\x02\x00\x02I\x00\x03ageL\x00\buserNamet\x00\x12Ljava/lang/String;xp\x00\x00\x00\x14t\x00\x05user1"
```
**JacksonJsonRedisSerializer序列化**

被序列化对象不需要实现Serializable接口，被序列化的结果清晰，容易阅读，而且存储字节少，速度快
存储内容如下：
```json
"{\"userName\":\"user1\",\"age\":20}"
```
**StringRedisSerializer序列化**

一般如果key、value都是string字符串的话，就是用这个就可以了

