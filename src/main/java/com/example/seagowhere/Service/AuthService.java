package com.example.seagowhere.Service;

import com.example.seagowhere.Exception.PasswordBlankException;
import com.example.seagowhere.Model.User;
import com.example.seagowhere.Repository.UserRepository;
import com.example.seagowhere.dto.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    // signs up a user and stores the user to the Users table
    public RequestResponse signUp(RequestResponse registrationRequest) {

        RequestResponse requestResponse = new RequestResponse();

        User users = new User();
        users.setFirstName(registrationRequest.getName());
        users.setEmail(registrationRequest.getEmail());

        if (registrationRequest.getPassword().isBlank()) {
            throw new PasswordBlankException();
        }

        users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        users.setRole(registrationRequest.getRole());
        User usersResult = userRepository.save(users);

        if (usersResult != null && usersResult.getUserId() > 0) {
            requestResponse.setUsers(usersResult);
            requestResponse.setMessage("User saved successfully.");
        }

        return requestResponse;
    }

    // logs in the authenticated user
    public RequestResponse signIn(RequestResponse signinRequest) {

        RequestResponse requestResponse = new RequestResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
        );

        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

        String jwt = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

        requestResponse.setToken(jwt);
        requestResponse.setRefreshToken(refreshToken);
        requestResponse.setExpirationTime("24Hr");
        requestResponse.setMessage("Signed in successfully.");

        return requestResponse;
    }

    // refresh a token for an authenticated user
    public RequestResponse refreshToken(RequestResponse refreshTokenRequest) {

        RequestResponse requestResponse = new RequestResponse();

        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());

        User user = userRepository.findByEmail(ourEmail).orElseThrow();

        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
            String jwt = jwtUtils.generateToken(user);
            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshTokenRequest.getToken());
            requestResponse.setExpirationTime("24Hr");
            requestResponse.setMessage("Refreshed token successfully.");
        }

        return requestResponse;
    }

    // returns the user information
    public RequestResponse profile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User users = userRepository.findByEmail(authentication.getName()).orElseThrow();

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setName(users.getFirstName());
        requestResponse.setEmail(users.getEmail());
        requestResponse.setPassword(users.getPassword());

        return requestResponse;
    }
}

