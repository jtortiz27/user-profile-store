package com.ortiz.userprofilestore.api.controller;

import com.ortiz.userprofilestore.api.model.UserResource;
import com.ortiz.userprofilestore.service.UserService;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<UserResource> retrieveAllUsers() {
        return userService.retrieveAllUsers()
                .map(UserResource::new);
    }

    @GetMapping(value = "/{id}")
    public Mono<UserResource> retrieveUser(@PathVariable("id") String id) {
        return userService.retrieveUserById(id)
                .map(UserResource::new);
    }

    @PostMapping
    public Mono<UserResource> createUser(@RequestBody UserResource userResource) {
        if (!isUserResourceWithAllRequiredFields(userResource)) {
            return Mono.error(new IllegalArgumentException("Must supply all required fields"));
        }

        return userService.createUser(userResource.getUserName(), userResource.getPassword(), userResource.getFirstName(), userResource.getLastName(), userResource.getRoles().get(0), userResource.getPointsOfContact())
                .map(UserResource::new);

    }

    @PatchMapping(value = "/{userName}")
    public Mono<UserResource> updateUser(@PathVariable("userName") String userName, @RequestBody UserResource userResource) {
        PointsOfContact pointsOfContact = userResource.getPointsOfContact();

        return userService.updateUserPointsOfContact(userName, pointsOfContact)
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
