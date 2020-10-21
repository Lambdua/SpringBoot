package com.lt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangtao
 * @Date 2020/7/15
 **/
@RestController
@RequestMapping("/api/example")
public class ExampleController {
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
