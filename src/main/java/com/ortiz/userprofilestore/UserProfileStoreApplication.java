package com.ortiz.userprofilestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class UserProfileStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProfileStoreApplication.class, args);
    }

}
