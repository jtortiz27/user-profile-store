package com.ortiz.userprofilestore.service;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {

    Flux<User> retrieveAllUsers();

    Mono<User> retrieveUserById(String id);

    Mono<User> createUser(String firstName, String lastName, List<Role> roles, PointsOfContact pointsOfContact);

    Mono<User> updateUser();

}
