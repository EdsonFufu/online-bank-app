package com.simplilearn.project.onlinebankapp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private String accountName;

    private String accountNumber;

    @Builder.Default
    private String currency = "TZS";

    @Builder.Default
    private double balance = 0.00;

    @Builder.Default
    private String accountType = "savings";  // savings or currency

    @Builder.Default
    private boolean status = true;

    private long customerId;
}
