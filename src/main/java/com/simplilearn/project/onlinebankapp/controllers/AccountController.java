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
    public Account get(@PathVariable("id") int id){
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
    public String delete(@PathVariable("id") String idInString){
        int id = Integer.parseInt(idInString);

        if(accountRepository.existsById(id)){
            Account account = accountRepository.getById(id);
            accountRepository.delete(account);
            return "Account with id [" + id + "] deleted Successfully";
        }
        return "Account with Id " + id + " Not found";
    }
}
