package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired private UserService userService;
    @GetMapping("/{id}")
    public User get(@PathVariable("id") int id){
        return userService.findById(id);
    }
    @GetMapping
    public ModelAndView index(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = "6") int size){
        ModelAndView modelAndView = new ModelAndView("user/index");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("users",userService.getPage(pageNumber, size));
        return modelAndView;
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        if(userService.exists(id)){
            userService.deleteById(id);
            return "User with id [" + id + "] deleted Successfully";
        }
        return "User with Id " + id + " Not found";
    }
}
