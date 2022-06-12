package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
    private final UserRepository userRepository;
    @PostMapping
    public User create(@RequestBody User user){
        return userRepository.save(user);
    }
    @PutMapping
    public User update(@RequestBody User user){
        return userRepository.save(user);
    }
    @GetMapping("/{id}")
    public User get(@PathVariable("id") int id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }
    @GetMapping
    public List<User> index(){
        return userRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String idInString){
        int id = Integer.parseInt(idInString);

        if(userRepository.existsById(id)){
            User user = userRepository.getById(id);
            userRepository.delete(user);
            return "User with id [" + id + "] deleted Successfully";
        }
        return "User with Id " + id + " Not found";
    }
}
