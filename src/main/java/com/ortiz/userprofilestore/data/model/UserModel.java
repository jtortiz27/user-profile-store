package com.ortiz.userprofilestore.data.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.ortiz.userprofilestore.service.model.PointsOfContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    @Field
    private String id;
    private String docType;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private PointsOfContact pointsOfContact;
}
