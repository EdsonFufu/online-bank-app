package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    @PostMapping
    public Account create(@RequestBody Account account){
        return accountRepository.save(account);
    }
    @PutMapping
    public Account update(@RequestBody Account account){
        return accountRepository.save(account);
    }
    @GetMapping("/{id}")
    public Account get(@PathVariable("id") long id){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        return null;
    }
    @GetMapping
    public List<Account> index(){
        return accountRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){

        if(accountRepository.existsById(id)){
            Account account = accountRepository.getById(id);
            accountRepository.delete(account);
            return "Account with id [" + id + "] deleted Successfully";
        }
        return "Account with Id " + id + " Not found";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody String payload){
        return "";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody String payload){
        return "";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestBody String payload){
        return "";
    }

    @PostMapping("/request-cheque-book")
    public String requestChequeBook(@RequestBody String payload){
        return "";
    }

}
