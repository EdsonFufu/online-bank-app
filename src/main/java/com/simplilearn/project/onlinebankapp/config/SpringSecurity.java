package com.simplilearn.project.onlinebankapp.config;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpSession;
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    public final CustomUserDetailsService customUserDetailsService;

    public final PasswordEncoder passwordEncoder;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**","/webjars/**","/css/**","/js/**","/images/**","/error","/webfonts/**","/webfonts");
    }

    @Autowired
    @Lazy
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {  // (2)
        http
                .authorizeRequests()
                //.antMatchers("/signup","/login","/resources/**", "/static/**","/webjars/**","/css/**","/js/**","/images/**","/error").permitAll()
                .antMatchers("/user-role","/user-role/**","/user","/user/**","/setting","/setting/**").hasRole("ADMIN")
                .antMatchers("/","/transaction","/transaction/**","/account","/account/**","/deposit","/balance").hasAnyRole("TELLER","ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin(form -> form
                        .loginPage("/login").usernameParameter("username").passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // run custom logics upon successful login
                            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                            String username = userDetails.getUsername();

                            System.out.println("The user " + username + " has logged in.");

                            response.sendRedirect(request.getContextPath());
                        })
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace();
                            String username = request.getParameter("username");
                            String error = exception.getMessage();
                            log.info("A failed login attempt with username: " + username + ". Reason: " + error);
                            String redirectUrl = request.getContextPath() + "/login?error=true";
                            response.sendRedirect(redirectUrl);
                        })
                        .permitAll()
                )
                .logout().logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/login?error=true");
                })
                .and()
                .csrf().disable();
    }

}
