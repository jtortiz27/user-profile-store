package com.ortiz.userprofilestore.api.model;

import com.ortiz.userprofilestore.data.model.Follow;
import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.service.model.Permission;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResource {
    private String id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
    private Set<Permission> permissions;
    private PointsOfContact pointsOfContact;
    private Set<Follow> follows;

    public UserResource(User user) {
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles();
        this.permissions = user.getPermissions();
        this.pointsOfContact = user.getPointsOfContact();
        this.follows = user.getFollows();
    }
}
