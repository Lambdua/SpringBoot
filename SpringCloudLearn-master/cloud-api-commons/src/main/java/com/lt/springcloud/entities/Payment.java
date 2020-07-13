package com.lt.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * (Payment)实体类
 *
 * @author liangtao
 * @since 2020-06-02 15:41:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment implements Serializable {
    private static final long serialVersionUID = 867848974344737048L;
    /**
    * @ID
    */
    private Long id;

    private String serial;


}
