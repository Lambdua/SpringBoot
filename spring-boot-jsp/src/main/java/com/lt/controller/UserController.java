package com.lt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liangtao
 * @description
 * @date 2021年07月22 17:24
 **/
@Controller
public class UserController {
    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public ModelAndView getUsers() {
        return new ModelAndView("getUsers");
    }

    @GetMapping("")
    public ModelAndView index(){
        return new ModelAndView("/index");
    }
}
