package com.lt.config;

import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangtao
 * @description 统一异常拦截处理,这里前后端分离的框架，加上@ResponseBody注解，不然对于返回值，
 * 会走视图解析器进行解析，而我们没有视图解析器，所以抛500异常。
 * @date 2021年07月21 15:13
 **/
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object RuntimeExceptionHandler(RuntimeException e){
        System.out.println("检查异常："+e.getMessage());
        return JSONUtil.createObj().putOpt("msg",e.getMessage());
    }
}
