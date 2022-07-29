package com.simplilearn.project.onlinebankapp.utils;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.Transaction;
import com.simplilearn.project.onlinebankapp.service.AccountService;
import com.simplilearn.project.onlinebankapp.service.TransactionService;
import com.simplilearn.project.onlinebankapp.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class AccountUtil {
    public boolean transfer(String username, String source, String destination, double amount, String desc, String narration, AccountService accountService, TransactionService transactionService, UserService userService){

        Account destinationAccount = accountService.findByAccountNumber(destination);

        double balance = destinationAccount.getBalance();

        double newBalance = balance + amount;

        destinationAccount.setBalance(newBalance);

        Account accountCR = accountService.save(destinationAccount);

        Account sourceAccount = accountService.findByAccountNumber(source);

        double mainAccountBalance = sourceAccount.getBalance();

        double mainAccountNewBalance = mainAccountBalance - amount;

        sourceAccount.setBalance(mainAccountNewBalance);

        Account accountDR = accountService.save(sourceAccount);

        Transaction txnDR = Transaction.builder()
                .sourceAccount(source)
                .destAccount(destination)
                .amount(String.valueOf(amount))
                .description(desc)
                .narration(narration)
                .balance(sourceAccount.getFormatedBalance())
                .drcr("DR")
                .user(userService.findUserByUsername(username))
                .build();

        Transaction txnCR = Transaction.builder()
                .sourceAccount(source)
                .destAccount(destination)
                .amount(String.valueOf(amount))
                .description(desc)
                .narration(narration)
                .balance(destinationAccount.getFormatedBalance())
                .drcr("CR")
                .user(userService.findUserByUsername(username))
                .build();

        List<Transaction> transactionList = transactionService.saveAll(new ArrayList<>() {{
            add(txnDR);
            add(txnCR);
        }});
        return transactionList.size() == 2;
    }
}
