package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user-role")
public class RoleController {
    @Autowired private UserRoleService userRoleService;
//    @PostMapping
//    public UserRole create(@RequestBody UserRole userRole){
//        return userRoleService.save(userRole);
//    }
    @GetMapping("/create")
    public ModelAndView create(@ModelAttribute("userRole") UserRole userRole){
        ModelAndView modelAndView = new ModelAndView("role/create");
        modelAndView.addObject("userRole",UserRole.builder().build());
        return modelAndView;
    }
//    @GetMapping("/{id}")
//    public UserRole get(@PathVariable("id") long id){
//        return userRoleService.findById(id);
//    }
    @GetMapping
    public ModelAndView index(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = "6") int size){
        ModelAndView modelAndView = new ModelAndView("role/index");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("userRoles",userRoleService.getPage(pageNumber, size));
        return modelAndView;
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){
        if(userRoleService.deleteById(id)){
            return "Roles with id [" + id + "] deleted Successfully";
        }
        return "Roles with Id " + id + " Not found";
    }
}
