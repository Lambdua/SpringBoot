package com.lt.springLearn.common;

import java.util.Arrays;
import java.util.List;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public class MQConstans {
    public static final String SIMPLE_ROUTER_KEY ="simple.hello";
    public static final String WORK_ROUTER_KEY ="work.hello";

    public static final String FANOUT_EXCHANGE_NAME ="exchange.fanout";

    public static final String DIRECT_EXCHANGE_NAME ="exchange.direct";
    public static final List<String> DIRECT_ROUTER_KEYS= Arrays.asList("orange","black","green");


    public static final String TOPIC_EXCHANGE_NAME="exchange.topic";
    public static final List<String> TOPIC_BINDING_KEYS=Arrays.asList(
            "*.orange.*",
            "*.*.rabbit",
            "lazy.#"
    );
    public static final List<String> TOPIC_ROUTER_KEYs=Arrays.asList(
            "a.orange.b", //0
            "a.b.rabbit", //1
            "lazy.a.b", //2
            "a.orange.rabbit", //0,1
            "lazy.orange.a",//0,2
            "lazy.a.rabbit",//1,2
            "lazy.orange.rabbit" //0,1,2
    );


}
