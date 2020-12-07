package com.lt.http.json.pojo;

/**
 * 支持的运输方式。 *“国际”运送方式只能用于运送地址在美国境外的订单，在这种情况下，是其中一种。
 *
 * @author 梁先生
 */
public enum Shipping {
    /**
     * 标准邮件
     */
    STANDARD_MAIL,
    /**
     * 优先邮件
     */
    PRIORITY_MAIL,
    /**
     * 国际邮件
     */
    INTERNATIONAL_MAIL,
    /**
     * 国内快递
     */
    DOMESTIC_EXPRESS,
    /**
     * 国际快递
     */
    INTERNATIONAL_EXPRESS
}
