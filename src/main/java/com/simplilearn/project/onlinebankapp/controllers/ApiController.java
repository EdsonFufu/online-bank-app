package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.CheckBookRequest;
import com.simplilearn.project.onlinebankapp.entities.Transfer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    @PostMapping("/account/transfer")
    public ResponseEntity<Transfer> transfer(@RequestBody Transfer transfer){
        return null;
    }

    @GetMapping("/request-check-book")
    public ResponseEntity<String> checkBook(@RequestBody CheckBookRequest checkBookRequest){
        return null;
    }

    @GetMapping("/transaction/{id}")
    public String transaction(@RequestBody String payload){
        return "";
    }

    @PostMapping("/request-cheque-book")
    public String requestChequeBook(@RequestBody String payload){
        return "";
    }
}
