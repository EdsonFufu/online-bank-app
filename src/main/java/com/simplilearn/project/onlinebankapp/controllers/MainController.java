package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.utils.AuthUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping({"/","/welcome","/home"})
    public ModelAndView index(ModelAndView modelAndView){
        AuthUtils.isAuthenticated(SecurityContextHolder.getContext().getAuthentication(), modelAndView);
        if(modelAndView.getModel().containsKey("error")){
            return modelAndView;
        }
        modelAndView.setViewName("index");
        modelAndView.addObject("title","Home");
        return modelAndView;
    }
}
