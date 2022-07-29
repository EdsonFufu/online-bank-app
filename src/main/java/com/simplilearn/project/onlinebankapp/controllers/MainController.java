package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.Settings;
import com.simplilearn.project.onlinebankapp.repository.SettingsRepository;
import com.simplilearn.project.onlinebankapp.service.CheckBookRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    CheckBookRequestService checkBookRequestService;
    @GetMapping(value = {"/","/welcome","/home"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("title","Home");
        return modelAndView;
    }

    @GetMapping("/bank/setting")
    public ModelAndView settings(){
        ModelAndView modelAndView = new ModelAndView("settings");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        Example<Settings> example = Example.of(Settings.builder().build());

        modelAndView.addObject("settings", settingsRepository.findOne(example).orElse(Settings.builder().build()));

        return modelAndView;
    }

    @PostMapping("/bank/setting")
    public ModelAndView saveSettings(@ModelAttribute("settings") Settings settings){
        ModelAndView modelAndView = new ModelAndView("settings");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        if(settings.getId() != null && settings.getId() > 0){
            Settings savedSettings = settingsRepository.getReferenceById(settings.getId());

            savedSettings.setCompanyName(settings.getCompanyName());
            savedSettings.setAccountNumber(settings.getAccountNumber());
            savedSettings.setAddress(settings.getAddress());
            savedSettings.setMobile(settings.getMobile());
            savedSettings.setWithdrawFee(settings.getWithdrawFee());
            settingsRepository.save(savedSettings);


        }else {

            settingsRepository.save(settings);
        }

        modelAndView.setViewName("redirect:/bank/setting");

        return modelAndView;
    }
    @GetMapping("/check-book-request")
    public ModelAndView checkBookRequests(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        ModelAndView modelAndView = new ModelAndView("check-book-request/index");
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("checkBookRequests",checkBookRequestService.getPage(pageNumber, size));
        return modelAndView;
    }
}
