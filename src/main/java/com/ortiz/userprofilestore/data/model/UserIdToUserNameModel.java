package com.ortiz.userprofilestore.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdToUserNameModel {
    @PrimaryKeyColumn(value = "userId", type = PrimaryKeyType.PARTITIONED)
    private String userId;

    private String userName;
}
