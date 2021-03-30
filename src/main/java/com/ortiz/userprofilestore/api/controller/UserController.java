package com.ortiz.userprofilestore.api.controller;

import com.ortiz.userprofilestore.api.model.UserResource;
import com.ortiz.userprofilestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

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
        this.allUsersFlux = userService.retrieveAllUsers().log(null, Level.INFO, SignalType.ON_NEXT).subscribeOn(Schedulers.elastic()).map(UserResource::new).cache(Duration.ofMinutes(1));
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
    public Flux<UserResource> createUser(@RequestBody Flux<UserResource> userResources) {
        return userResources.flatMap(user -> {
            if (!isUserResourceWithAllRequiredFields(user)) {
                return Mono.error(new IllegalArgumentException("Must supply all required fields"));
            }
            return userService.createUser(user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getRoles(), user.getPointsOfContact());
        }).map(UserResource::new);
    }

    @PatchMapping(value = "/{userName}")
    public Mono<UserResource> updateUser(@PathVariable("userName") String userName, @RequestBody Mono<UserResource> userResource) {
        return userResource.flatMap(user -> userService.updateUser(userName, user.getPointsOfContact(), user.getRoles(), user.getPermissions(), user.getFollows()))
                .map(UserResource::new);
    }

    @DeleteMapping(value = "/{userName}")
    public Mono<ResponseEntity<?>> deleteUser(@PathVariable("userName") String userName) {
        return userService.deleteUser(userName)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @GetMapping(value = "/{userName}/roles")
    public Mono<UserResource> retrieveRolesForUser(@PathVariable("userName") String userName) {
        return userService.retrieveUserByUserName(userName)
                .map(user -> {
                    UserResource userResource = new UserResource();
                    userResource.setFirstName(user.getFirstName());
                    userResource.setLastName(user.getLastName());
                    userResource.setRoles(user.getRoles());
                    userResource.setId(user.getId());
                    return userResource;
                });
    }

    @GetMapping(value = "/{userName}/permissions")
    public Mono<UserResource> retrievePermissionsForUser(@PathVariable("userName") String userName) {
        return userService.retrievePermissionsByUser(userName)
                .map(permissions -> {
                    UserResource userResource = new UserResource();
                    userResource.setPermissions(permissions);
                    return userResource;
                });

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
