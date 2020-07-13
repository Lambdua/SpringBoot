package com.lt.springLearn.controller;

import com.lt.springLearn.common.ResultVo;
import com.lt.springLearn.service.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangtao
 * @Date 2020/7/9
 **/
@RestController
@RequestMapping("sentinel")
public class SentinelUseController {
    @Autowired
    SentinelService sentinelService;
    @GetMapping("message/{id}")
    public ResultVo<String> getMessage(@PathVariable("id")String id){
        return ResultVo.succeed(sentinelService.sayHello(id),"成功");
    }
}
