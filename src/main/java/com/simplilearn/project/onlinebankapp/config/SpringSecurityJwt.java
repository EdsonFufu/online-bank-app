package com.simplilearn.project.onlinebankapp.config;

import com.simplilearn.project.onlinebankapp.service.CustomUserDetailsService;
import com.simplilearn.project.onlinebankapp.utils.AuthTokenFilter;
import com.simplilearn.project.onlinebankapp.utils.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration("SpringSecurityJwt")
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1000)
public class SpringSecurityJwt extends WebSecurityConfigurerAdapter {

    public final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private AuthTokenFilter authTokenFilter;



    public final PasswordEncoder passwordEncoder;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {  // (2)
        http.csrf().disable()
                .httpBasic().and()
                .authorizeRequests()
                //.antMatchers("/signup","/login","/resources/**", "/static/**","/webjars/**","/css/**","/js/**","/images/**","/error").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);



        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
