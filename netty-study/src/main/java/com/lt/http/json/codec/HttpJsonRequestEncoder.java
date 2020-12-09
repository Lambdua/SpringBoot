package com.lt.http.json.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
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

    /**
     * 从一条消息编码到另一条消息。对于此编码器可以处理的每条书面消息，将调用此方法。
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param msg the message to encode to an other one
     * @param out 应该在其中添加编码消息的列表需要进行某种汇总
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws UnknownHostException {
        //将业务需要发送的PoJo对象Order实例通过OM转为json，并封装成ByteBuf
        ByteBuf body = encode0(ctx, msg.getBody());
        FullHttpRequest request = msg.getRequest();
        //如果业务自定义定制了消息头，则不进行初始化http消息头
        if (request == null) {
            request=initialDefaultRequest(body);
        }
        //由于请求消息消息体不为空，也没有使用Chunk方式，所以需要在Http消息头中设置消息体的长度Content-Length
        HttpUtil.setContentLength(request, body.readableBytes());
        //完成json序列化后将重新构造的Http请求消息加入到out中，由后续Netty的Http请求编码器继续对Http请求消息进行编码
        out.add(request);
    }

    private FullHttpRequest initialDefaultRequest(ByteBuf body) throws UnknownHostException {
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1
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
        return request;
    }
}

