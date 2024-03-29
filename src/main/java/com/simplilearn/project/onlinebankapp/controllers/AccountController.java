package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.AccountDTO;
import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.Deposit;
import com.simplilearn.project.onlinebankapp.entities.Withdraw;
import com.simplilearn.project.onlinebankapp.service.AccountService;
import com.simplilearn.project.onlinebankapp.service.SettingService;
import com.simplilearn.project.onlinebankapp.service.TransactionService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.AccountUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired private AccountService accountService;
    @Autowired private UserService userService;
    @Autowired private TransactionService transactionService;
    @Autowired private SettingService settingService;
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
        modelAndView.addObject("deposit",Deposit.builder().build());
        modelAndView.addObject("withdraw",Withdraw.builder().build());
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostMapping("/deposit")
    public ModelAndView deposit(@ModelAttribute("deposit") Deposit deposit,
                                 @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = "6") int size
        ){
        ModelAndView modelAndView = new ModelAndView("account/view");

        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account mainAccount = accountService.findByAccountNumber(settingService.getSettings().getAccountNumber());

        String narration = "Transfer " + String.format("TZS %,.2f",deposit.getAmount()) +" from [" + mainAccount.getAccountNumber() + "] to [" + deposit.getAccountNumber() + "]";

        boolean success = new AccountUtil().transfer(userDetails.getUsername(), mainAccount.getAccountNumber(), deposit.getAccountNumber(), deposit.getAmount(),deposit.getDescription(),narration,accountService,transactionService,userService);

        if(success) {
            modelAndView.addObject("message", narration+" successfully");
            modelAndView.setViewName("redirect:/account/view/" + accountService.findByAccountNumber(deposit.getAccountNumber()).getId());
        }else {
            modelAndView.addObject("message", narration + " Failed");
            modelAndView.setViewName("account/index");
            modelAndView.addObject("accounts",accountService.getPage(pageNumber, size));
        }
        return modelAndView;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostMapping("/withdraw")
    public ModelAndView withdraw(@ModelAttribute("deposit") Withdraw withdraw,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "size", required = false, defaultValue = "6") int size
    ){
        ModelAndView modelAndView = new ModelAndView("account/view");

        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account mainAccount = accountService.findByAccountNumber(settingService.getSettings().getAccountNumber());

        String narration = "Withdraw " + String.format("TZS %,.2f",withdraw.getAmount()) +" from [" + withdraw.getAccountNumber() + "]";

        boolean success = new AccountUtil().transfer(userDetails.getUsername(), withdraw.getAccountNumber(),  mainAccount.getAccountNumber(), withdraw.getAmount(),withdraw.getDescription(),narration,accountService,transactionService,userService);

        if(success) {
            modelAndView.addObject("message", narration+" successfully");
            modelAndView.setViewName("redirect:/account/view/" + accountService.findByAccountNumber(withdraw.getAccountNumber()).getId());
        }else {
            modelAndView.addObject("message", narration + " Failed");
            modelAndView.setViewName("account/index");
            modelAndView.addObject("accounts",accountService.getPage(pageNumber, size));
        }
        return modelAndView;
    }

}
