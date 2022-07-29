package com.simplilearn.project.onlinebankapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    private String sourceAccount;
    private String destinationAccount;
    private String amount;
    private String description;
}
