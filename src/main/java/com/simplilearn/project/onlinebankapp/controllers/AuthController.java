package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.*;
import com.simplilearn.project.onlinebankapp.repository.UserRepository;
import com.simplilearn.project.onlinebankapp.service.UserRoleService;
import com.simplilearn.project.onlinebankapp.service.UserService;
import com.simplilearn.project.onlinebankapp.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 33600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserService userService;
	@Autowired
	UserRoleService userRoleService;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtTokenUtil jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginReq loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			if (authentication.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);

				UserDetails userDetails = (UserDetails) authentication.getPrincipal();

				User user = userService.findUserByUsername(userDetails.getUsername());
				List<String> roles = userDetails.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());

				Account account = user.getAccounts().get(0);

				return ResponseEntity.ok(LoginRes.builder().jwt(jwt).userId(String.valueOf(user.getId())).email(user.getEmail()).username(user.getUsername()).roles(roles).account(account.getAccountNumber()).balance(account.getFormatedBalance()).build());
			}

		}catch (Exception e){
			log.error(e.getMessage());
		}

		return ResponseEntity.status(403).body(LoginRes.builder().build());
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpReq signUpRequest) {
		if (userService.findUserByUsername(signUpRequest.getUsername()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageRes("Error: Username is already taken!"));
		}
		if (userService.findUserByEmail(signUpRequest.getEmail()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageRes("Error: Email is already in use!"));
		}
		// Create new user's account
		User user = User.builder().username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.build();
		List<UserRole> roles = new ArrayList<>(){{
			add(UserRole.builder().role("ROLE_CUSTOMER").build());
		}};

		user.setUserRoles(roles);
		userService.save(user);
		return ResponseEntity.ok(new MessageRes("User created successfully!"));
	}
}