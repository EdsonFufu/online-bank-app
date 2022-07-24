package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.Transaction;
import com.simplilearn.project.onlinebankapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transaction")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    @PostMapping
    public Transaction create(@RequestBody Transaction transaction){
        return transactionRepository.save(transaction);
    }

    @GetMapping("/{id}")
    public Transaction get(@PathVariable("id") long id){
        return transactionRepository.getById(id);
    }
    @GetMapping("/")
    public List<Transaction> index(){
        return transactionRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){
        if(transactionRepository.existsById(id)){
            Transaction transaction = transactionRepository.getById(id);
            transactionRepository.delete(transaction);
            return "Transaction with id [" + id + "] deleted Successfully";
        }
        return "Transaction with Id " + id + " Not found";
    }
}
