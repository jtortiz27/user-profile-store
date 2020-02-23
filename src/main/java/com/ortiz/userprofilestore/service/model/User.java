package com.ortiz.userprofilestore.service.model;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.data.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private List<Permission> permissions;
    private PointsOfContact pointsOfContact;

    public User(UserModel userModel) {
        this.id = userModel.getId();
        this.userName = userModel.getUserName();
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.roles = userModel.getRoles();
        this.pointsOfContact = userModel.getPointsOfContact();
    }
}
