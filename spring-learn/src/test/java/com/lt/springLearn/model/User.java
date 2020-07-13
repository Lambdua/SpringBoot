package com.lt.springLearn.model;

import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liangtao
 * @Date 2020/7/6
 **/
@Data
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = RandomUtil.randomInt(0,10000);
    public String username;
    public LocalDateTime birth;
    public Long age;
}
