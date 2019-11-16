package com.ortiz.userprofilestore.api.controller;

import com.ortiz.userprofilestore.api.model.UserResource;
import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.service.UserService;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController (UserService userService) {
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
        String firstName = userResource.getFirstName();
        String lastName = userResource.getLastName();
        List<Role> roles = userResource.getRoles();
        PointsOfContact pointsOfContact = userResource.getPointsOfContact();

        if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName) || StringUtils.isEmpty(roles) || pointsOfContact == null || pointsOfContact.getEmailAddresses().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Must supply all required fields"));
        }

        return userService.createUser(firstName, lastName, roles, pointsOfContact)
                .map(UserResource::new);

    }

    @PatchMapping(value = "/{id}")
    public Mono<UserResource> updateUser(@PathVariable("id") String id, @RequestBody UserResource userResource) {
        String firstName = userResource.getFirstName();
        String lastName = userResource.getLastName();
        List<Role> roles = userResource.getRoles();
        PointsOfContact pointsOfContact = userResource.getPointsOfContact();
        return userService.updateUser()
                .map(UserResource::new);

    }
}
