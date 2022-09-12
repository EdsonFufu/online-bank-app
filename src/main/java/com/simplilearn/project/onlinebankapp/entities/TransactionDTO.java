package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private String destinationAccount;
    private String amount;
    private String balance;
    private boolean status;
    private String transactionDate;
}
