package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.AccountDTO;
import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.Settings;
import com.simplilearn.project.onlinebankapp.repository.SettingsRepository;
import com.simplilearn.project.onlinebankapp.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    SettingsRepository settingsRepository;
    @GetMapping(value = {"/","/welcome","/home"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        AuthUtils.isAuthenticated(SecurityContextHolder.getContext().getAuthentication(), modelAndView);
        if(modelAndView.getModel().containsKey("error")){
            return modelAndView;
        }
        modelAndView.addObject("title","Home");
        return modelAndView;
    }

    @GetMapping("/bank/setting")
    public ModelAndView settings(){
        ModelAndView modelAndView = new ModelAndView("settings");

        Example<Settings> example = Example.of(Settings.builder().build());

        modelAndView.addObject("settings", settingsRepository.findOne(example).orElse(Settings.builder().build()));

        return modelAndView;
    }

    @PostMapping("/bank/setting")
    public ModelAndView saveSettings(@ModelAttribute("settings") Settings settings){
        ModelAndView modelAndView = new ModelAndView("settings");
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
}
