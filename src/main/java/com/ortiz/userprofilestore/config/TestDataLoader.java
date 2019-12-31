package com.ortiz.userprofilestore.config;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.data.model.UserModel;
import com.ortiz.userprofilestore.data.repository.UserRepository;
import com.ortiz.userprofilestore.service.model.EmailAddress;
import com.ortiz.userprofilestore.service.model.PhoneNumber;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.DecimalFormat;

@Configuration
public class TestDataLoader {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createInitialUsers(CouchbaseOperations couchbaseOperations) {
        return args -> {
            DecimalFormat format = new DecimalFormat("0000");
            for (int i = 0; i < 1000; i++) {
                PointsOfContact pointsOfContact = new PointsOfContact();
                pointsOfContact.getEmailAddresses().add(new EmailAddress("jason" + i + "@google.com", "google"));
                pointsOfContact.getPhoneNumbers().add(new PhoneNumber("1", "973744" + format.format(i)));
                couchbaseOperations.save(new UserModel("userName" + i, passwordEncoder.encode("encodedPassword"), "Jason", "Ortiz", Role.SUPER_ADMIN, pointsOfContact));
            }
        };
    }
}
