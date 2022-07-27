package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.AccountDTO;
import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.repository.AccountRepository;
import com.simplilearn.project.onlinebankapp.service.AccountService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired private AccountService accountService;
    @Autowired private UserService userService;
    @GetMapping("/create")
    public ModelAndView create(){
        ModelAndView modelAndView = new ModelAndView("account/create");


        AccountDTO account = AccountDTO.builder().accountNumber(accountService.getAccountNumber()).accountName("Faida Account").build();
        modelAndView.addObject("account", account);
        modelAndView.addObject("users",userService.findAll());
        return modelAndView;
    }
    @PutMapping
    public Account update(@RequestBody Account account){
        return accountService.save(account);
    }
    @GetMapping("/view/{id}")
    public ModelAndView get(@PathVariable("id") long id) throws NotFoundException {
        ModelAndView modelAndView = new ModelAndView("account/view");
        Account account  = accountService.findById(id);
        if(account != null){
            modelAndView.addObject("account",account);
            return modelAndView;
        }
        throw  new NotFoundException("Account is not found");
    }
    @GetMapping
    public ModelAndView index(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = "6") int size){
        ModelAndView modelAndView = new ModelAndView("account/index");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("accounts",accountService.getPage(pageNumber, size));
        return modelAndView;
    }

    @PostMapping
    public ModelAndView save(@ModelAttribute("account") AccountDTO accountDTO){
        ModelAndView modelAndView = new ModelAndView("account/view");
        Account account = Account.builder()
                .accountNumber(accountDTO.getAccountNumber())
                .accountName(accountDTO.getAccountName())
                .balance(accountDTO.getBalance())
                .currency(accountDTO.getCurrency())
                .accountType(accountDTO.getAccountType())
                .user(userService.findById(accountDTO.getCustomerId()))
                .build();

        Account savedAccount = accountService.save(account);

        if(savedAccount != null) {
            modelAndView.addObject("message", "Account [" + savedAccount.getId() + "] has been updated successfully");
            modelAndView.setViewName("redirect:/account/view/" + savedAccount.getId());
        }else {
            modelAndView.addObject("message", "Failed to create Account...");
            modelAndView.setViewName("account/create");
            modelAndView.addObject("user",AccountDTO.builder().build());
        }
        return modelAndView;
    }

}
