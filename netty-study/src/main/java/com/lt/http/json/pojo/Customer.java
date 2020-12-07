package com.lt.http.json.pojo;

import lombok.Data;

import java.util.List;

/**
 * Customer 信息.
 *
 * @author 梁先生
 */
@Data
public class Customer {
    private long customerNumber;

    /**
     * 姓
     */
    private String firstName;

    /**
     * 名
     */
    private String lastName;

    /**
     * 中间名，如果存在
     */
    private List<String> middleNames;
}
