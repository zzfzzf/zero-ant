package com.test;

import io.Application;
import io.zzy.zero.Spider;
import io.zzy.zero.UserMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@PropertySource(value = "classpath:application.yml")
public class Test {
    @Autowired
    UserMapper userMapper;
    @org.junit.Test
    public void kao() {
       Spider spider = userMapper.selectById(1);
        System.out.println(spider.getUrl());
    }
}
