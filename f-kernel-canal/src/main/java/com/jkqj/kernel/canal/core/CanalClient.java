package com.jkqj.kernel.canal.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class CanalClient {

    @Autowired
    private CanalConsumer canalConsumer;

    @PostConstruct
    public void init() {
        canalConsumer.start();
    }

    @PreDestroy
    public void destroy() {
        canalConsumer.stopGracefully();
    }

}
