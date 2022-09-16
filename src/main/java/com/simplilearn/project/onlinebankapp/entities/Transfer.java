package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private String description;
}
