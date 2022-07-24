package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG = "User with username %s not found";

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = user.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
        log.info("user:{}[{}] and role:{}",username,user.getFirstName() + " " + user.getLastName() ,roleNames);

        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream().map(UserRole::getRole).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
               true, true, true, true, grantedAuthorities);
    }

    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElse(null);
    }
}
