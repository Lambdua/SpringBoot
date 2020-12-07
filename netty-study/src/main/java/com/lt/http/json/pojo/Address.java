package com.lt.http.json.pojo;

import lombok.Data;

/**
 * Address 信息.
 *
 * @author 梁先生
 */
@Data
public class Address {
    /**
     * 街道信息的第一行（必填）。
     */
    private String street1;

    /**
     * 街道信息的第二行（可选）。
     */
    private String street2;

    private String city;

    /**
     * 州名缩写（美国和加拿大必需，否则为可选）。
     */
    private String state;

    /**
     * 邮政编码（美国和加拿大需要，否则为可选）。
     */
    private String postCode;

    /**
     * 国家/地区名称（可选，如果未提供，则假定为美国）。
     */
    private String country;
}
