package com.lt.http.json.pojo;

import lombok.Data;

/**
 * Order 信息.
 *
 * @author 梁先生
 */
@Data
public class Order {

    private long orderNumber;

    private Customer customer;

    /**
     * 账单地址信息。
     */
    private Address billTo;

    private Shipping shipping;

    /**
     * 收货地址信息。如果缺少，帐单地址也将用作送货地址。
     */
    private Address shipTo;

    private Float total;
}
