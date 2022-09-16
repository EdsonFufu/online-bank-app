package com.simplilearn.project.onlinebankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class IcinOnlineBankAppApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(IcinOnlineBankAppApplication.class, args);
    }

    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
