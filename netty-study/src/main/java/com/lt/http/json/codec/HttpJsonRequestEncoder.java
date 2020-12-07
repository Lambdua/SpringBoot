package com.lt.http.json.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author 梁先生
 * @description http json请求的编码器
 * @Date 2020/12/7
 **/
public class HttpJsonRequestEncoder extends AbstractHttpJsonEncoder<HttpJsonRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws UnknownHostException {
        ByteBuf body = encode0(ctx, msg.getBody());
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            initialDefaultRequest(request, body);
        }
        HttpUtil.setContentLength(request, body.readableBytes());
        out.add(request);
    }

    private void initialDefaultRequest(FullHttpRequest request, ByteBuf body) throws UnknownHostException {
        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1
                , HttpMethod.GET, "/do", body);
        HttpHeaders headers = request.headers();
        headers.set(HttpHeaderNames.HOST, InetAddress.getLocalHost().getHostAddress());
        /**
         *  在http1.1中，client和server都是默认对方支持长链接的， 如果client使用http1.1协议，但又不希望使用长链接，
         *  则需要在header中指明connection的值为close；如果server方也不想支持长链接，
         *  则在response中也需要明确说明connection的值为close.
         *  不论request还是response的header中包含了值为close的connection，
         *  都表明当前正在使用的tcp链接在当天请求处理完毕后会被断掉。
         *  以后client再进行新的请求时就必须创建新的tcp链接了。
         */
        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        //支持的编码类型
        headers.set(HttpHeaderNames.ACCEPT_ENCODING,
                HttpHeaderValues.GZIP.toString() + ','
                        + HttpHeaderValues.DEFLATE.toString());
        //期望字符集
        headers.set(HttpHeaderNames.ACCEPT_CHARSET,
                "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        headers.set(HttpHeaderNames.ACCEPT_LANGUAGE, "zh");
        headers.set(HttpHeaderNames.USER_AGENT,
                "Netty json Http Client side");
        //指定客户端能够接收的内容类型
        headers.set(HttpHeaderNames.ACCEPT,
                "text/html,application/json;q=0.9,*/*;q=0.8");
    }
}

