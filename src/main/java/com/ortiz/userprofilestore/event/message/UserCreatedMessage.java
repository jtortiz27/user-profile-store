package com.ortiz.userprofilestore.event.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedMessage {
    private String userId;
    private String userName;
}
