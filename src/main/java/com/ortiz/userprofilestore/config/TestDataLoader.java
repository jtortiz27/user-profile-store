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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

@Configuration
public class TestDataLoader {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ExecutorService executorService;

    @Bean
    CommandLineRunner createInitialUsers(CouchbaseOperations couchbaseOperations) {
        return args -> {
            DecimalFormat format = new DecimalFormat("0000");
            Flux<UserModel> users = Flux.range(0, 50)
                    .flatMap(i -> {
                        PointsOfContact pointsOfContact = new PointsOfContact();
                        pointsOfContact.getEmailAddresses().add(new EmailAddress("jason" + i + "@google.com", "google"));
                        pointsOfContact.getPhoneNumbers().add(new PhoneNumber("1", "9733739586" + format.format(i)));

                        return userRepository.save(new UserModel("userName" + i, passwordEncoder.encode("encodedPassword"), "Jason", "Ortiz", Role.SUPER_ADMIN, pointsOfContact)).flux();
                    })
                    .subscribeOn(Schedulers.fromExecutorService(executorService))
                    .log(null, Level.INFO, SignalType.ON_NEXT);
            users.blockLast();
        };
    }
}
