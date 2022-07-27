package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.ChangePassword;
import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private enum ROLE {
        ROLE_ADMIN,
        ROLE_CUSTOMER,
        ROLE_TELLER
    }
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired private UserService userService;
    @GetMapping("/view/{id}")
    public ModelAndView get(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView("user/view");
        modelAndView.addObject("user",userService.findById(id));
        return modelAndView;
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
    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public ModelAndView profile(){

        ModelAndView modelAndView = new ModelAndView("user/profile");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByUsername(userDetails.getUsername());
        modelAndView.addObject("user", user);
        modelAndView.addObject("changePassword", ChangePassword.builder().build());
        return modelAndView;
    }
    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("id") long id){

        ModelAndView modelAndView = new ModelAndView("user/update");
        User user = userService.findById(id);
        modelAndView.addObject("user", user);
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
    @PostMapping(
            value = "/update",
            consumes = "application/x-www-form-urlencoded;charset=UTF-8"
    )
    public ModelAndView update(@ModelAttribute("user") User userDTO, BindingResult bindingResult) {

        User userDB = userService.findById(userDTO.getId());
        userDB.setFirstName(userDTO.getFirstName());
        userDB.setLastName(userDTO.getLastName());
        userDB.setEmail(userDTO.getEmail());
        userDB.setAddress(userDTO.getAddress());
        userDB.setMobile(userDTO.getMobile());
        if(!userDB.getPassword().equals(userDTO.getPassword())){
            userDB.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        ModelAndView modelAndView = new ModelAndView("user/view");
        User updatedUser = userService.save(userDB);
        if(updatedUser != null) {
            modelAndView.addObject("message", "User [" + updatedUser.getId() + "] has been updated successfully");
            modelAndView.setViewName("redirect:/user/view/" + updatedUser.getId());
        }else {
            modelAndView.addObject("message", "Failed to register User...");
            modelAndView.setViewName("user/update");
            modelAndView.addObject("user",userDB);
        }
        return modelAndView;
    }

}
