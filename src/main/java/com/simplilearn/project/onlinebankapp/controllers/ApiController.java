package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.*;
import com.simplilearn.project.onlinebankapp.service.AccountService;
import com.simplilearn.project.onlinebankapp.service.CheckBookRequestService;
import com.simplilearn.project.onlinebankapp.service.TransactionService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.AccountUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 33600)
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired UserService userService;
    @Autowired CheckBookRequestService checkBookRequestService;

    @Autowired TransactionService transactionService;

    @Autowired AccountService accountService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/check-book-request")
    public ResponseEntity<MessageRes> checkBookRequests(@RequestBody(required = false) String request){

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CheckBookRequest checkBookRequest = CheckBookRequest.builder()
                .user(userService.findUserByUsername(userDetails.getUsername()))
                .build();
        if(checkBookRequestService.save(checkBookRequest) != null){
            return ResponseEntity.ok(new MessageRes("Check Book Request Sent Successfully"));
        }
        return ResponseEntity.ok(new MessageRes("Failed To Send Check Book Request"));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/check-book-request")
    public ResponseEntity<List<CheckBookRequestDTO>> checkBookRequest(){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userService.findUserByUsername(userDetails.getUsername()).getId();
        log.info("UserId:{}",userId);
        List<CheckBookRequest> bookRequestList = checkBookRequestService.findAll().stream().filter(checkBookRequest -> checkBookRequest.getUser().getId() == userId).collect(Collectors.toList());
        if(bookRequestList.size() > 0){
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(bookRequestList.stream().map(
                    checkBookRequest -> CheckBookRequestDTO.builder().id(checkBookRequest.getId()).approved(checkBookRequest.isApproved()).collected(checkBookRequest.isCollected()).createdDate(checkBookRequest.getCreatedDate().toString()).collectedDate(checkBookRequest.getCollectedDate()).customerName(checkBookRequest.getUser().getFirstName() + " "+ checkBookRequest.getUser().getLastName()).build()).collect(Collectors.toList())
            );
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
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account account = userService.findUserByUsername(userDetails.getUsername()).getAccounts().stream().findFirst().orElse(null);

        log.info("My Account:{}",account);

        if(account != null) {


            List<Transaction> transactions = transactionService.findAll().stream().filter(
                    transaction -> (transaction.getSourceAccount().equals(account.getAccountNumber()) || transaction.getDestAccount().equals(account.getAccountNumber()))
            ).collect(Collectors.toList());
            if (transactions.size() > 0) {
                return ResponseEntity.ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(transactions);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostMapping("/account/transfer")
    public ResponseEntity<MessageRes> transfer(@RequestBody Transfer transfer){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String narration = "Transfer " + String.format("TZS %,.2f",transfer.getAmount()) +" from [" + transfer.getSourceAccount() + "] to [" + transfer.getDestinationAccount() + "]";
        boolean success = new AccountUtil().transfer(userDetails.getUsername(),transfer.getSourceAccount(),transfer.getDestinationAccount(), transfer.getAmount(),transfer.getDescription(),narration,accountService,transactionService,userService);
        if(success){
            log.info("Transfer Successfull");
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(new MessageRes(narration + " Successfully"));
        }
        log.info("Transfer Failed");
        return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(new MessageRes(narration + " Failed"));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/user/profile")
    public ResponseEntity<UserDTO> profile() throws NotFoundException {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.findUserByUsername(userDetails.getUsername());
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .username(user.getUsername())
                .accounts(user.getAccounts().stream().map(user1 -> AccountDTOAPI.builder().name(user1.getAccountName()).accnumber(user1.getAccountNumber()).balance(user1.getFormatedBalance()).status(user1.getUser().isEnabled()).build()).collect(Collectors.toList()))
                .transactions(user.getTransactions().stream().filter(transaction -> transaction.getUser().getId() == user.getId()).collect(Collectors.toList()))
                .build();
        if(userDTO != null){
            return ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(userDTO);
        }
        throw new NotFoundException("User Not Found");
    }

}
