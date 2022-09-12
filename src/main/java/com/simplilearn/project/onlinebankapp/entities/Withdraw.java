package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Withdraw {
    private String accountNumber;
    private double amount;
    private String description;
}
