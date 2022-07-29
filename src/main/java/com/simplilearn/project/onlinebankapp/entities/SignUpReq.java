package com.simplilearn.project.onlinebankapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReq {
    private enum Role {
        ROLE_CUSTOMER,
        ROLE_ADMIN,
        ROLE_TELLER
    }
    private String username;
    private String password;
    private String email;
}

