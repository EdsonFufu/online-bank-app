package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.repository.UserRoleRepository;
import com.simplilearn.project.onlinebankapp.service.UserRoleService;
import com.simplilearn.project.onlinebankapp.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user-role")
public class RoleController {
    private final UserRoleService userRoleService;
    @PostMapping
    public UserRole create(@RequestBody UserRole userRole){
        return userRoleService.save(userRole);
    }
    @PutMapping
    public UserRole update(@RequestBody UserRole userRole){
        return userRoleService.save(userRole);
    }
    @GetMapping("/{id}")
    public UserRole get(@PathVariable("id") long id){
        return userRoleService.findById(id);
    }
    @GetMapping("/")
    public ModelAndView index(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = "6") int size){
        ModelAndView modelAndView = new ModelAndView("role/index");
        AuthUtils.isAuthenticated(SecurityContextHolder.getContext().getAuthentication(), modelAndView);
        if(modelAndView.getModel().containsKey("error")){
            return modelAndView;
        }
        modelAndView.addObject("users",userRoleService.getPage(pageNumber, size));
        return modelAndView;
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){
        if(userRoleService.existsById(id)){
            UserRole roles = userRoleService.getById(id);
            userRoleService.delete(roles);
            return "Roles with id [" + id + "] deleted Successfully";
        }
        return "Roles with Id " + id + " Not found";
    }
}
