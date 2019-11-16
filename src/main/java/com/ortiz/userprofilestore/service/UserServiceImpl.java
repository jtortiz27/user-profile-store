package com.ortiz.userprofilestore.service;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.data.repository.UserRepository;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Flux<User> retrieveAllUsers() {
        return userRepository.findAll()
                .map(User::new);
    }

    @Override
    public Mono<User> retrieveUserById(String id) {
        return userRepository.findById(id)
                .map(User::new);
    }

    @Override
    public Mono<User> createUser(String firstName, String lastName, List<Role> roles, PointsOfContact pointsOfContact) {
        return userRepository.findUserModelsByEmailAddress(pointsOfContact.getEmailAddresses().get(0).getEmail())
                .single()
                .map(User::new);
    }

    @Override
    public Mono<User> updateUser() {
        return Mono.empty();
    }

}
