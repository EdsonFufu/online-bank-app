package com.simplilearn.project.onlinebankapp.controllers;


import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.repository.UserRoleRepository;
import com.simplilearn.project.onlinebankapp.service.UserRoleService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private enum ROLE {
        ROLE_ADMIN,
        ROLE_CUSTOMER,
        ROLE_TELLER
    }


    @RequestMapping(value={ "/login"})
    public ModelAndView login(@RequestParam(value = "error", required = false) boolean error){
        ModelAndView modelAndView = new ModelAndView("login");

        if(error){
            modelAndView.addObject("user", User.builder().build());
            modelAndView.addObject("errorMessage","Login Failed!... Invalid Username OR Password...");
            return modelAndView;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            modelAndView.addObject("user",User.builder().build());
            return modelAndView;
        }else {
            return new ModelAndView("redirect:/");
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            session.invalidate();
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }




        model.addAttribute("title", "Logout");
        return "redirect:/login";
    }


    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("message", "SignUp");
        modelAndView.addObject("user", User.builder().build());
        return modelAndView;
    }

    @PostMapping(
            value = "/signup",
            consumes = "application/x-www-form-urlencoded;charset=UTF-8"
    )
    public ModelAndView createNewUser(@ModelAttribute("user") User userDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (userService.findUserByUsername(userDTO.getUsername())) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        } else {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            log.info(userDTO.toString());
            User user = User.builder()
                    .firstName(userDTO.getFirstName())
                    .lastName(userDTO.getLastName())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .mobile(userDTO.getMobile())
                    .email(userDTO.getEmail())
                    .build();

            user.setUserRoles(new ArrayList<>(){{
                add(UserRole.builder().role(ROLE.ROLE_ADMIN.name()).build());
            }});


            log.info(user.toString());

            User registeredUser = userService.save(user);

            modelAndView.addObject("user", User.builder().build());
            if(registeredUser != null) {
                modelAndView.addObject("message", "User [" + registeredUser.getId() + "] has been registered successfully");
                modelAndView.setViewName("login");
            }else {
                modelAndView.addObject("message", "Failed to register User...");
                modelAndView.setViewName("register");
            }

        }
        return modelAndView;
    }

    @GetMapping (value = {"/users","/users/index"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("error","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("user/index");
        modelAndView.addObject("users",userService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        modelAndView.setViewName("403");

        if (auth.getPrincipal() != null) {

            modelAndView.addObject("userInfo", auth.getPrincipal());

            String message = "Hi <b>" + auth.getName() + "</b> You do not have permission to access this page!";
            modelAndView.addObject("errorMessage", message);
            modelAndView.addObject("errorCode", "403");

        }

        return modelAndView;
    }




}