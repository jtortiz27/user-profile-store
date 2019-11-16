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

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestDataLoader {

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner createInitialUsers(CouchbaseOperations couchbaseOperations) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.SUPER_ADMIN);
        return args -> couchbaseOperations.save(new UserModel("1", "user", "Jason", "Ortiz", roles, new PointsOfContact()));
    }
}
