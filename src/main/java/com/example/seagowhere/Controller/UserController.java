package com.example.seagowhere.Controller;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.User;
import com.example.seagowhere.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Integer userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable(value = "id") Integer userId,
                                           @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Integer userId) {
        User checkUser = userService.getUserById(userId).map(user -> {
            userService.deleteUser(user.getUserId());
            return user;
        }).orElseThrow(() -> new ResourceNotFoundException());

        String response = String.format("User %s deleted successfully", checkUser.getUserName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam(value = "email") String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}