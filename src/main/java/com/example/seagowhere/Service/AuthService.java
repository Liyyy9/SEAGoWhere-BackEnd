package com.example.seagowhere.Service;

import com.example.seagowhere.Exception.PasswordBlankException;
import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.EnumRole;
import com.example.seagowhere.Model.Users;
import com.example.seagowhere.Repository.UserRepository;
import com.example.seagowhere.dto.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RequestResponse signUp(RequestResponse registrationRequest){

        RequestResponse requestResponse = new RequestResponse();

        Users users = new Users();

        users.setFirstName(registrationRequest.getFirstName());
        users.setLastName(registrationRequest.getLastName());

        users.setEmail(registrationRequest.getEmail());

        if(registrationRequest.getPassword().isBlank())
            throw new PasswordBlankException();

        users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        users.setRole(registrationRequest.getRole());
        Users usersResult = userRepository.save(users);

        if (usersResult != null && usersResult.getId()>0) {
            requestResponse.setUsers(usersResult);
            requestResponse.setMessage("User saved successfully.");
        }

        return requestResponse;
    }

    // logs in the authenticated user
    public RequestResponse signIn(RequestResponse signinRequest){

        RequestResponse requestResponse = new RequestResponse();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));

        var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

        var jwt = jwtUtils.generateToken(user);

        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

        requestResponse.setToken(jwt);
        requestResponse.setRefreshToken(refreshToken);
        requestResponse.setExpirationTime("24Hr");
        requestResponse.setMessage("Signed in successfully.");

        return requestResponse;
    }

    // update the authenticated user
    @Transactional
    public RequestResponse update(String userName, RequestResponse updateRequest){

        RequestResponse requestResponse = new RequestResponse();

        Users usersResult = userRepository.findByEmail(userName).orElseThrow(()->new ResourceNotFoundException());

        usersResult.setFirstName(updateRequest.getFirstName());
        usersResult.setLastName(updateRequest.getLastName());
        usersResult.setEmail(updateRequest.getEmail());

        if (updateRequest.getPassword() == null || updateRequest.getPassword().isEmpty())
            throw new PasswordBlankException();
        else
            usersResult.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

        var updatedPersonnel = userRepository.save(usersResult);

        if (updatedPersonnel != null && updatedPersonnel.getId()>0) {

            requestResponse.setUsers(updatedPersonnel);

            var jwt = jwtUtils.generateToken(updatedPersonnel);

            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), updatedPersonnel);

            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshToken);
            requestResponse.setExpirationTime("24Hr");

            if(updatedPersonnel.getRole() == EnumRole.ADMIN)
                requestResponse.setMessage("Admin updated successfully.");
            else
                requestResponse.setMessage("User updated successfully.");
        }

        return requestResponse;

    }

    // refresh a token for an authenticated user
    public RequestResponse refreshToken(RequestResponse refreshTokenRequest){

        RequestResponse requestResponse = new RequestResponse();

        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());

        Users users = userRepository.findByEmail(ourEmail).orElseThrow();

        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshTokenRequest.getToken());
            requestResponse.setExpirationTime("24Hr");
            requestResponse.setMessage("Refreshed token successfully.");
        }

        return requestResponse;
    }

    // returns the user information
    public RequestResponse profile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication); //prints the details of the user(name,email,password,roles e.t.c)
        System.out.println(authentication.getDetails()); // prints the remote ip
        System.out.println(authentication.getName()); //prints the EMAIL, the email was stored as the unique identifier

        var users = new Users();
        users = userRepository.findByEmail(authentication.getName()).orElseThrow();

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setFirstName(users.getFirstName());
        requestResponse.setLastName(users.getLastName());
        requestResponse.setEmail(users.getEmail());
        requestResponse.setPassword(users.getPassword());
        requestResponse.setRole(users.getRole());

        return requestResponse;

    }

}