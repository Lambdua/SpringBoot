package com.lt.httpFile;

import cn.hutool.core.io.FileUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author 梁先生
 * @description HTTP文件服务业务处理类
 * @Date 2020/12/3
 **/
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        //首先对请求的消息解码结果进行判断，如果解码失败，直接返回400
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        //对方法进行判断 不符合返回405
        if (request.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String requestUri = request.uri();
        final String path = sanitizeUri(requestUri);
        // url不合法，返回403
        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }


        File file = new File(path);
        //文件不存在或者是隐藏文件，返回404
        if (!FileUtil.exist(file) || file.isHidden()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        //如果是目录，返回目录的链接给客户端浏览器
        if (file.isDirectory()) {
            if (requestUri.endsWith("/")) {
                sendListing(ctx, file);
            } else {
                //没有"/"，重定向
                sendRedirect(ctx, requestUri + "/");
            }
            return;
        }
        if (file.isFile()) {
            //下载文件操作
            downloadFile(ctx, file, request);
            return;
        }
        // 其余情况
        sendError(ctx, FORBIDDEN);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private void downloadFile(ChannelHandlerContext ctx, File file, FullHttpRequest request) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
            long fileLength = randomAccessFile.length();
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            HttpUtil.setContentLength(response, fileLength);
            setContentTypeHeader(response, file);
            if (HttpUtil.isKeepAlive(request)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);
            //这里通过ChunkedFile对象将文件直接写入到发送缓冲区中。
            ChannelFuture sendFileFuture = ctx.write(
                    new ChunkedFile(randomAccessFile, 0, fileLength, 8192),
                    ctx.newProgressivePromise());
            //添加传输进度的监听器
            sendFileFuture.addListener(new ProgressiveFutureListener());
            //使用chunked编码进行传输时，最后需要发送一个编码结束的空消息体。
            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!HttpUtil.isKeepAlive(request)) {
                //对于非keep-alive的连接，最后一包消息发送完成之后，服务端要主动关闭连接。
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (FileNotFoundException e) {
            //文件未找到，返回404
            sendError(ctx, NOT_FOUND);
        } catch (IOException e) {
            //io异常，返回500
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure:" + status + "\r\n", StandardCharsets.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 对请求过来的uri进行解析判断
     *
     * @param uri 请求传过来的uri
     * @return java.lang.String
     * @author liangtao
     * @date 2020/12/5
     **/
    private String sanitizeUri(String uri) {
        //1. uri解码
        try {
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, StandardCharsets.ISO_8859_1.name());
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException("url编码格式不支持，请使用 utf-8或者iso_8859_1格式编码");
            }
        }
        //2. uri合法性校验，判断是否实在允许访问的url及其子目录
        if (!uri.startsWith(url) || !uri.startsWith("/")) {
            return null;
        }
        //3. 将硬编码的文件路径分隔符替换为系统文件路径分隔符
        uri = uri.replace('/', File.separatorChar);

        //4. 对uri进行第二次合法校验,不允许特殊字符 或特殊组合
        if (uri.contains(File.separator + '.')
                || uri.contains('.' + File.separator) || uri.startsWith(".")
                || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        //5. 最后返回当前工程运行的目录+uri目录作为path
        return System.getProperty("user.dir") + uri;
    }

    /**
     * 展示文件列表
     *
     * @author liangtao
     * @date 2020/12/5
     **/
    private static void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //设置响应头信息为html
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append(" 目录：");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append(dirPath).append(" 目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        //这里是上级目录
        buf.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            buf.append("<li>链接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, StandardCharsets.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }

    private class ProgressiveFutureListener implements ChannelProgressiveFutureListener {

        @Override
        public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
            if (total < 0) { // total unknown
                System.err.println("Transfer progress: " + progress);
            } else {
                System.err.println("Transfer progress: " + progress + " / " + total);
            }
        }

        @Override
        public void operationComplete(ChannelProgressiveFuture future) {
            System.out.println("传输完成。");
        }
    }
}
