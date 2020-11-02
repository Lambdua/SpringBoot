package com.lt.redis.model;

import lombok.*;

/**
 * @author liangtao
 * @description 测试实体Bean
 * @Date 2020年11月02 13:40
 **/
@Data
@AllArgsConstructor()
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class PersonTest {
    private String name;
    private String addr;
    private Integer age;
    private Double height;
}
