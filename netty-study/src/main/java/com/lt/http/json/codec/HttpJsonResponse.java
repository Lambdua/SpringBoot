package com.lt.http.json.codec;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 梁先生
 * @description http json响应解析
 * @Date 2020/12/7
 **/
@Data
@AllArgsConstructor
public class HttpJsonResponse {
    private FullHttpResponse response;
    private Object result;
}
