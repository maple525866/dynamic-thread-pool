package com.threadPool;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * @author maple
 * @Description
 * @createTime:2025-04-14 22:34
 */

@SpringBootApplication(exclude = {RedissonAutoConfiguration.class})
@Configurable
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
