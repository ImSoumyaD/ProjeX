package com.soumya.Project.Management.controller;

import com.soumya.Project.Management.JwtUtils.JwtProvider;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.UserRepository;
import com.soumya.Project.Management.response.AuthResponse;
import com.soumya.Project.Management.service.CustomUserDetailsImpl;
import com.soumya.Project.Management.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsImpl customUserDetails;
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User user) throws Exception {
        User isExist = userRepository.findByEmail(user.getEmail());
        if (isExist != null){
            throw new Exception("user already exists with this email");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setProjectCount(0);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(newUser);
        subscriptionService.createSubscription(savedUser);//create a free subscription for the user

        return new ResponseEntity<>("User create successfully",HttpStatus.CREATED);
    }

    @GetMapping("/signin")
    public AuthResponse signIn(@RequestBody User user) {
        try {
            Authentication authentication = authenticate(user.getEmail(),user.getPassword());
            String token = JwtProvider.generateToken(authentication);
            return new AuthResponse(token,"log in success");
        }catch (Exception e){
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);
        if (userDetails == null){
            throw new UsernameNotFoundException("user not found with email");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("wrong password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
