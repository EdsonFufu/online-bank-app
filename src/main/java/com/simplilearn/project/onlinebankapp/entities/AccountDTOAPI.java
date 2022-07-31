package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTOAPI {
    private String name;
    private String accnumber;
    private String balance;
    private boolean status;
}
