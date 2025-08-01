package com.honlife.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class HonlifeApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HonlifeApplication.class, args);
    }

}
