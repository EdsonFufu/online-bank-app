package com.simplilearn.project.onlinebankapp.entities;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deposit {
    private String accountNumber;
    private double amount;
    private String Description;
}
