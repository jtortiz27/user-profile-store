package com.ortiz.userprofilestore.service;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> retrieveAllUsers();

    Mono<User> retrieveUserByUserName(String userName);

    Mono<User> createUser(String userName, String password, String firstName, String lastName, Role role, PointsOfContact pointsOfContact);

    Mono<User> updateUserPointsOfContact(String userName, PointsOfContact pointsOfContact);

    Mono<Void> deleteUser(String userName);

}
