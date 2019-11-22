package com.ortiz.userprofilestore.data.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    @Field
    private String userName;
    private String password;
    private String id;
    private String docType;
    private String firstName;
    private String lastName;
    private List<Role> roles = new ArrayList<>();
    private PointsOfContact pointsOfContact;

    public UserModel(String userName, String encodedPassword, String firstName, String lastName) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = encodedPassword;
        this.docType = "user";
        this.firstName = firstName;
        this.roles = getDefaultRoles();
        this.pointsOfContact = new PointsOfContact();
    }

    public UserModel(String userName, String encodedPassword, String firstName, String lastName, List<Role> roles, PointsOfContact pointsOfContact) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = encodedPassword;
        this.docType = "user";
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.pointsOfContact = pointsOfContact;
    }

    public UserModel(String userName, String encodedPassword, String firstName, String lastName, Role role, PointsOfContact pointsOfContact) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = encodedPassword;
        this.docType = "user";
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles.add(role);
        this.pointsOfContact = pointsOfContact;
    }

    private static List<Role> getDefaultRoles() {
        return Arrays.asList(Role.MEMBER);
    }

}
