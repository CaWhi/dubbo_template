package com.dubbo.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class testServiceImp implements testService {
    private static final Logger LOGGER = LoggerFactory.getLogger(testServiceImp.class);

    @Override
    public String sayHello(String name) {
        LOGGER.info("name:{}",name);

        return "Hello World！1111"+name;
    }
}
