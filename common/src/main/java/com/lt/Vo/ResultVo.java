package com.lt.Vo;

import cn.hutool.core.builder.EqualsBuilder;
import cn.hutool.core.builder.HashCodeBuilder;
import com.lt.enums.CodeEnum;

import java.io.Serializable;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
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

    public T getData() {
        return this.data;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ResultVo)) {
            return false;
        }

        ResultVo<?> resultVo = (ResultVo<?>) o;

        return new EqualsBuilder()
                .append(data, resultVo.data)
                .append(code, resultVo.code)
                .append(msg, resultVo.msg)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(data)
                .append(code)
                .append(msg)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ResultVo(data=" + this.getData() + ", code=" + this.getCode() + ", msg=" + this.getMsg() + ")";
    }

    public ResultVo() {
    }

    public ResultVo(T data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }
}
