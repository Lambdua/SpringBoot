package com.lt.mq.controller;

import com.lt.Vo.ResultVo;
import com.lt.mq.direct.DirectSender;
import com.lt.mq.fanout.FanoutSender;
import com.lt.mq.simple.SimpleSender;
import com.lt.mq.topic.TopicSender;
import com.lt.mq.work.WorkSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangtao
 * @Date 2020/6/23
 **/
@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    @Autowired
    private SimpleSender simpleSender;
    @Autowired
    private WorkSender workSender;
    @Autowired
    private FanoutSender fanoutSender;
    @Autowired
    private DirectSender directSender;
    @Autowired
    private TopicSender topicSender;

    @GetMapping("simple")
    public ResultVo<String> simpleTest() {
        for (int i = 0; i < 10; i++) {
            simpleSender.send(String.valueOf(i));
        }
        return ResultVo.succeed();
    }

    @GetMapping("work")
    public ResultVo workTest() {
        for (int i = 0; i < 10; i++) {
            workSender.send(String.valueOf(i));
        }
        return ResultVo.succeed();
    }


    @GetMapping("fanout")
    public ResultVo fanoutTest() {
        for (int i = 0; i < 10; i++) {
            fanoutSender.send(String.valueOf(i));
        }
        return ResultVo.succeed();
    }

    @GetMapping("direct")
    public ResultVo directTest() {
        for (int i = 0; i < 10; i++) {
            directSender.send(String.valueOf(i));
        }
        return ResultVo.succeed();
    }

    @GetMapping("topic")
    public ResultVo topicTest() {
/*
        for (int i = 0; i < 2; i++) {
            topicSender.send(String.valueOf(i));
        }
*/
        topicSender.send(" ");
        return ResultVo.succeed();

    }
}
