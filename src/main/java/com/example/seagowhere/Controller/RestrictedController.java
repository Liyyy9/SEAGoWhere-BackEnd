package com.example.seagowhere.Controller;

import com.example.seagowhere.Service.AuthService;
import com.example.seagowhere.dto.RequestResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restricted/api")
@CrossOrigin("*")
public class RestrictedController {

    @Autowired
    private AuthService authService;

    // restricted view for user or view profile info.
    @GetMapping("/profile")
    public ResponseEntity<RequestResponse> profileView(){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.profile());
    }

    // restricted view for user or administrator to update profile info.
    @PutMapping("/profile/update")
    public ResponseEntity<RequestResponse> updateProfile(@Valid @RequestBody RequestResponse requestResponse){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return ResponseEntity.status(HttpStatus.OK).body(authService.update(userName,requestResponse));
    }

}
