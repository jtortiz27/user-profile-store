package com.ortiz.userprofilestore.api.controller;

import com.ortiz.userprofilestore.api.model.UserResource;
import com.ortiz.userprofilestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.util.logging.Level;


@Slf4j
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    Flux<UserResource> allUsersFlux;

    public UserController(UserService userService) {
        this.userService = userService;
        this.allUsersFlux = userService.retrieveAllUsers().log(null, Level.INFO, SignalType.ON_NEXT).map(UserResource::new).cache(Duration.ofMinutes(1));
    }

    @GetMapping
    public Flux<UserResource> retrieveAllUsers() {
        return allUsersFlux;
    }

    @GetMapping(value = "/{userName}")
    public Mono<UserResource> retrieveUser(@PathVariable("userName") String userName) {
        return userService.retrieveUserByUserName(userName)
                .map(UserResource::new);
    }

    @PostMapping
    public Mono<UserResource> createUser(@RequestBody UserResource userResource) {
        return Mono.just(userResource)
                .flatMap(user -> {
                    if (!isUserResourceWithAllRequiredFields(userResource)) {
                        return Mono.error(new IllegalArgumentException("Must supply all required fields"));
                    }

                    return userService.createUser(userResource.getUserName(), userResource.getPassword(), userResource.getFirstName(), userResource.getLastName(), userResource.getRoles().get(0), userResource.getPointsOfContact())
                            .map(UserResource::new);
                });
    }

    @PatchMapping(value = "/{userName}")
    public Mono<UserResource> updateUser(@PathVariable("userName") String userName, @RequestBody UserResource userResource) {
        return userService.updateUserPointsOfContact(userName, userResource.getPointsOfContact())
                .map(UserResource::new);
    }

    private static boolean isUserResourceWithAllRequiredFields(UserResource userResource) {
        return !StringUtils.isEmpty(userResource.getUserName())
                && !StringUtils.isEmpty(userResource.getPassword())
                && !StringUtils.isEmpty(userResource.getFirstName())
                && !StringUtils.isEmpty(userResource.getLastName())
                && !CollectionUtils.isEmpty(userResource.getRoles())
                && !CollectionUtils.isEmpty(userResource.getPointsOfContact().getEmailAddresses());

    }
}
