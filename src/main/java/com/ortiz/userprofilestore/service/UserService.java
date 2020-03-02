package com.ortiz.userprofilestore.service;

import com.ortiz.userprofilestore.data.model.Follow;
import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.service.model.Permission;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserService {

    Flux<User> retrieveAllUsers();

    Mono<User> retrieveUserByUserName(String userName);

    Mono<User> createUser(String userName, String password, String firstName, String lastName, Set<Role> role, PointsOfContact pointsOfContact);

    Mono<User> updateUser(String userName, PointsOfContact pointsOfContact, Set<Role> roles, Set<Permission> permissions, Set<Follow> follow);

    Mono<Void> deleteUser(String userName);

    Mono<Set<Permission>> retrievePermissionsByUser(String userName);
}
