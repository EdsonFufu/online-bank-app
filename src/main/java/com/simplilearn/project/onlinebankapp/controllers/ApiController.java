package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.CheckBookRequest;
import com.simplilearn.project.onlinebankapp.entities.Transaction;
import com.simplilearn.project.onlinebankapp.entities.Transfer;
import com.simplilearn.project.onlinebankapp.exceptions.NotFoundException;
import com.simplilearn.project.onlinebankapp.service.AccountService;
import com.simplilearn.project.onlinebankapp.service.CheckBookRequestService;
import com.simplilearn.project.onlinebankapp.service.TransactionService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired UserService userService;
    @Autowired CheckBookRequestService checkBookRequestService;

    @Autowired TransactionService transactionService;

    @Autowired AccountService accountService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/request-check-book/{customerId}")
    public ResponseEntity<String> checkBookRequests(@RequestBody String request){

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CheckBookRequest checkBookRequest = CheckBookRequest.builder()
                .user(userService.findUserByUsername(userDetails.getUsername()))
                .build();
        if(checkBookRequestService.save(checkBookRequest) != null){
            return ResponseEntity.ok("Request Sent Successfully");
        }
        return ResponseEntity.ok("Failed To Send Request");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/request-check-book")
    public ResponseEntity<List<CheckBookRequest>> checkBookRequest(){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CheckBookRequest> bookRequestList = checkBookRequestService.findAll(userService.findUserByUsername(userDetails.getUsername()).getId());
        if(bookRequestList.size() > 0){
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(bookRequestList);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/transaction/{id}")
    public ResponseEntity<Transaction> transaction(@PathVariable("id") long id){
        Transaction transaction = transactionService.findById(id);
        if(transaction != null){
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(transaction);
        }
        return ResponseEntity.notFound().build();
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/transaction")
    public ResponseEntity<List<Transaction>> transactions(){
        List<Transaction> transactions = transactionService.findAll();
        if(transactions.size() > 0){
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(transactions);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostMapping("/account/transfer")
    public ResponseEntity<String> transfer(@RequestBody Transfer transfer){
        String narration = "Transfer " + String.format("TZS %,.2f",Double.parseDouble(transfer.getAmount())) +" from [" + transfer.getSourceAccount() + "] to [" + transfer.getDestinationAccount() + "]";
        boolean success = new AccountUtil().transfer("",transfer.getSourceAccount(),transfer.getDestinationAccount(), Double.parseDouble(transfer.getAmount()),transfer.getDescription(),narration,accountService,transactionService,userService);
        if(success){
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(narration + " Successfully");
        }
        return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(narration + " Failed");
    }

}
