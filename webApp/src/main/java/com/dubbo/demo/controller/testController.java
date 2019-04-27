package com.dubbo.demo.controller;

import com.dubbo.demo.dto.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class testController {
    private static final Logger LOGGER = LoggerFactory.getLogger(testController.class);

    @Autowired
    com.dubbo.demo.service.testService testService;

    @RequestMapping("/index")
    public String printHello(ModelMap model) {
        model.addAttribute("message", "Hello Spring MVC Framework!");
        return "home/index";
    }

    @RequestMapping("/test")
    @ResponseBody
    public ResultVo aaa(String name) {
        LOGGER.info("the argument name:{}",name);
        List<String> list=new ArrayList<>();
        list.add(testService.sayHello(name));
        return new ResultVo(200,"操作成功",list);
    }
}
