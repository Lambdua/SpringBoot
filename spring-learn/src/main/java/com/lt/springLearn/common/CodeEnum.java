package com.lt.springLearn.common;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
public enum CodeEnum {
    SUCCESS(0),
    ERROR(1);

    private Integer code;

    private CodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
