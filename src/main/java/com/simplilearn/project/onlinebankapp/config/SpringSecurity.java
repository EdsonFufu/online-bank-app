package com.simplilearn.project.onlinebankapp.config;

import com.simplilearn.project.onlinebankapp.service.CustomUserDetailsService;
import com.simplilearn.project.onlinebankapp.utils.AuthTokenFilter;
import com.simplilearn.project.onlinebankapp.utils.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpSession;
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    public final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private AuthTokenFilter authTokenFilter;



    public final PasswordEncoder passwordEncoder;

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**","/webjars/**","/css/**","/js/**","/images/**","/error","/webfonts/**","/webfonts","/signup");
    }

    @Override
    @Bean
    @Lazy
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/signup","/login","/resources/**", "/static/**","/webjars/**","/css/**","/js/**","/images/**","/error").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/user-role","/user-role/**","/user","/user/**","/setting","/setting/**","/","/transaction","/transaction/**","/account","/account/**","/deposit","/balance").hasAnyRole("TELLER","ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin(form -> form
                        .loginPage("/login").usernameParameter("username").passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // run custom logics upon successful login
                            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                            String username = userDetails.getUsername();

                            log.info("The user " + username + " has logged in.");

                            response.sendRedirect(request.getContextPath() + "/welcome");
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
                });



        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
