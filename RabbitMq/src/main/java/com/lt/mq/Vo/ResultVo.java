package com.lt.mq.Vo;

import com.lt.mq.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultVo<T> implements Serializable {
    private T data;
    private Integer code;
    private String msg;

    public static <T> ResultVo<T> succeed() {
        return succeed("操作成功");
    }

    public static <T> ResultVo<T> succeed(String msg) {
        return (ResultVo<T>) succeedWith((Object)null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResultVo<T> succeed(T model, String msg) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResultVo<T> succeed(T model) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), "");
    }

    public static <T> ResultVo<T> succeedWith(T data, Integer code, String msg) {
        return new ResultVo(data, code, msg);
    }

    public static <T> ResultVo<T> failed(String msg) {
        return (ResultVo<T>) failedWith((Object)null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> ResultVo<T> failed(T model, String msg) {
        return failedWith(model, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> ResultVo<T> failedWith(T data, Integer code, String msg) {
        return new ResultVo(data, code, msg);
    }

    public boolean success() {
        return this.getCode().equals(CodeEnum.SUCCESS.getCode());
    }
}
