package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePassword {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
