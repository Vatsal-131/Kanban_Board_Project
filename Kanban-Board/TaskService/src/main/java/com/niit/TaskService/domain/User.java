package com.niit.TaskService.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private Long phoneNumber;
    private String profilePic;
}
