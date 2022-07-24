package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user-role")
public class RoleController {
    private final UserRoleRepository userRoleRepository;
    @PostMapping
    public UserRole create(@RequestBody UserRole userRole){
        return userRoleRepository.save(userRole);
    }
    @PutMapping
    public UserRole update(@RequestBody UserRole userRole){
        return userRoleRepository.save(userRole);
    }
    @GetMapping("/{id}")
    public UserRole get(@PathVariable("id") long id){
        return userRoleRepository.getById(id);
    }
    @GetMapping
    public List<UserRole> index(){
        return userRoleRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){
        if(userRoleRepository.existsById(id)){
            UserRole roles = userRoleRepository.getById(id);
            userRoleRepository.delete(roles);
            return "Roles with id [" + id + "] deleted Successfully";
        }
        return "Roles with Id " + id + " Not found";
    }
}
