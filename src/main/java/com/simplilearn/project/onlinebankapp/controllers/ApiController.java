package com.simplilearn.project.onlinebankapp.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody String payload){
        return "";
    }

    @PostMapping("/account/transfer")
    public String transfer(@RequestBody String payload){
        return "";
    }

    @GetMapping("/transaction")
    public String transactions(){
        return "";
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
