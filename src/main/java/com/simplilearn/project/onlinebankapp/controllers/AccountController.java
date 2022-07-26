package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.repository.AccountRepository;
import com.simplilearn.project.onlinebankapp.service.AccountService;
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
    @GetMapping("/create")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView("account/create");
        modelAndView.addObject("message", "Create Account");
        modelAndView.addObject("user", User.builder().build());
        return modelAndView;
    }
    @PutMapping
    public Account update(@RequestBody Account account){
        return accountService.save(account);
    }
    @GetMapping("/{id}")
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

}
