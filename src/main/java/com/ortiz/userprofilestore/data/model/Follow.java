package com.ortiz.userprofilestore.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    private String id;
    private String name;
    private FollowType type;
}
