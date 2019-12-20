package com.ortiz.userprofilestore.config;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.data.model.UserModel;
import com.ortiz.userprofilestore.data.repository.UserRepository;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseOperations;

@Configuration
public class TestDataLoader {

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner createInitialUsers(CouchbaseOperations couchbaseOperations) {
        return args -> {
            for (int i = 0; i < 1000; i++) {
                couchbaseOperations.save(new UserModel("userName" + i, "encodedPassword", "Jason", "Ortiz", Role.SUPER_ADMIN, new PointsOfContact()));
            }
        };
    }
}
