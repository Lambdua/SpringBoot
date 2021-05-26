package com.lt.redis.interceptor;

import com.lt.redis.repository.RedisRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author liangtao
 * @description 点击次数收集器
 * @date 2021年05月25 11:31
 **/
public class ClickCollectInterceptor extends HandlerInterceptorAdapter {

    private RedisRepository redisRepository;
    public static final String INTERFACE_CLICK_NUMBER = "interface:click:number";

    /**
     * 60秒内访问
     */
    public static final String ACCESS_INTERFACE_60_SECOND = "access:interface:second:60:";

    public static final String ACCESS_INTERFACE_10_MINUTE = "access:interface:minute:10:";

    public static final String ACCESS_INTERFACE_30_MINUTE = "access:interface:minute:30:";

    public static final String ACCESS_INTERFACE_1_HOUR = "access:interface:hour:1:";

    public static final String ACCESS_INTERFACE_TOTAL = "access:interface:total:";

    public static final String ACCESS_INTERFACE_CONSUMER = "access:interface:CONSUMER:";

    public ClickCollectInterceptor(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    private static ThreadLocal<String> routerLocal =new ThreadLocal();
    private static ThreadLocal<Long> timeLocal =new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String router = request.getRequestURI();
        saveClickByTimeSlicing(router);
        saveClickInterfaceTotalNumber(router);
        routerLocal.set(router);
        timeLocal.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            String router = routerLocal.get();
            long consumer = System.currentTimeMillis() - timeLocal.get();
            redisRepository.opsForZSet().incrementScore(ACCESS_INTERFACE_CONSUMER,router,consumer);
            super.postHandle(request, response, handler, modelAndView);
        }finally {
            routerLocal.remove();
            timeLocal.remove();
        }
    }

    /**
     * 存储各个路由的访问总量
     * @author liangtao
     * @date 2021/5/26
     * @param router
     * @return void
     **/
    private void saveClickInterfaceTotalNumber(String router) {
        redisRepository.opsForZSet().incrementScore(ACCESS_INTERFACE_TOTAL,router,1D);
    }

    /**
     * 时间分片存储每个接口在各个时间分片的点击数量
     * @author liangtao
     * @date 2021/5/26
     * @param router 路由接口
     **/
    private void saveClickByTimeSlicing(String router) {
        List<Object> objects = redisRepository.getRedisTemplate().executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                long cur = System.currentTimeMillis();
                long temp = cur - cur % (60 * 1000);
                String time = LocalDateTime.ofInstant(Instant.ofEpochMilli(temp), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
                ZSetOperations zSetOperations = operations.opsForZSet();
                zSetOperations.incrementScore(ACCESS_INTERFACE_60_SECOND + router, time, 1D);
                temp = cur - cur % (10 * 60 * 1000);
                time = LocalDateTime.ofInstant(Instant.ofEpochMilli(temp), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
                zSetOperations.incrementScore(ACCESS_INTERFACE_10_MINUTE + router, time, 1D);
                temp = cur - cur % (30 * 60 * 1000);
                time = LocalDateTime.ofInstant(Instant.ofEpochMilli(temp), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
                zSetOperations.incrementScore(ACCESS_INTERFACE_30_MINUTE + router, time, 1D);
                temp = cur - cur % (60 * 60 * 1000);
                time = LocalDateTime.ofInstant(Instant.ofEpochMilli(temp), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd HH"));
                zSetOperations.incrementScore(ACCESS_INTERFACE_1_HOUR + router, time, 1D);
                return null;
            }
        });
    }

}
