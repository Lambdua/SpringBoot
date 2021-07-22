package com.lt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangtao
 * @description
 * @date 2021年07月22 10:24
 **/
@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
