package com.ortiz.userprofilestore.service.model;

import com.ortiz.userprofilestore.data.model.Follow;
import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.data.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
    private Set<Permission> permissions;
    private PointsOfContact pointsOfContact;
    private Set<Follow> follows;

    public User(UserModel userModel) {
        this.id = userModel.getId();
        this.userName = userModel.getUserName();
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.roles = userModel.getRoles();
        this.pointsOfContact = userModel.getPointsOfContact();
        this.follows = userModel.getFollowing();
    }
}
