package com.jkqj.kernel.canal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.jkqj"})
public class KernelApplication {

    public static void main(String[] args) {

        SpringApplication.run(KernelApplication.class, args);
    }
}

