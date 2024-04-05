package com.ortiz.userprofilestore.data.model;

import com.ortiz.userprofilestore.service.model.PointsOfContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
    private LocalDate joinDate;
    private Set<Role> roles = new HashSet<>();
    private PointsOfContact pointsOfContact;
    private Set<Follow> following = new HashSet<>();

    public UserModel(String userName, String encodedPassword, String firstName, String lastName) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = encodedPassword;
        this.docType = "user";
        this.firstName = firstName;
        this.roles = getDefaultRoles();
        this.pointsOfContact = new PointsOfContact();
    }

    public UserModel(String userName, String encodedPassword, String firstName, String lastName, Set<Role> roles, PointsOfContact pointsOfContact) {
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

    private static Set<Role> getDefaultRoles() {
        return Collections.singleton(Role.MEMBER);
    }

}
