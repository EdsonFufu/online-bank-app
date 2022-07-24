package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    @PostMapping
    public User create(@RequestBody User user){
        return userService.save(user);
    }
    @PutMapping
    public User update(@RequestBody User user){
        return userService.save(user);
    }
    @GetMapping("/{id}")
    public User get(@PathVariable("id") int id){
        return userService.findById(id);
    }
    @GetMapping
    public List<User> index(){
        return userService.findAll();
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
