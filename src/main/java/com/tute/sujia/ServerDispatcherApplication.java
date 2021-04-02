package com.tute.sujia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableCaching
@MapperScan("com.tute.sujia.dao")
public class ServerDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerDispatcherApplication.class, args);
    }

}
