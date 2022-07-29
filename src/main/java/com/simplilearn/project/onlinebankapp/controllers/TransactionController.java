package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.Deposit;
import com.simplilearn.project.onlinebankapp.entities.Transaction;
import com.simplilearn.project.onlinebankapp.repository.TransactionRepository;
import com.simplilearn.project.onlinebankapp.service.TransactionService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
    @Autowired private TransactionService transactionService;
    @GetMapping
    public ModelAndView index(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        ModelAndView modelAndView = new ModelAndView("transaction/index");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("transactions",transactionService.getPage(pageNumber, size));
        return modelAndView;
    }
    @GetMapping("/view/{id}")
    public ModelAndView get(@PathVariable("id") long id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("transaction/view");
        Transaction transaction  = transactionService.findById(id);
        if(transaction != null){
            modelAndView.addObject("transaction",transaction);
            return modelAndView;
        }
        throw  new NotFoundException("Transaction is not found");
    }
}
