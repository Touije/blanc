package com.ranblanc.blanc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlancApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlancApplication.class, args);
    }

}
