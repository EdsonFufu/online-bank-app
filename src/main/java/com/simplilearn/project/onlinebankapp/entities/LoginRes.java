package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRes {
    private String jwt;
    private String userId;
    private String username;
    private String email;
    private List<String> roles = new ArrayList<>();
    private Account account;
}
