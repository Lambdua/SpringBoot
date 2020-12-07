package com.lt.http.json.codec;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 梁先生
 * @description http json请求
 * @Date 2020/12/7
 **/
@AllArgsConstructor
@Data
public class HttpJsonRequest {
    private FullHttpRequest request;
    private Object body;
}
