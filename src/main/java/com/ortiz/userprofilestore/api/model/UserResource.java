package com.ortiz.userprofilestore.api.model;

import com.ortiz.userprofilestore.data.model.Role;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import com.ortiz.userprofilestore.service.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResource {
    private String id;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private PointsOfContact pointsOfContact;

    public UserResource(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles();
        this.pointsOfContact = user.getPointsOfContact();
    }
}
