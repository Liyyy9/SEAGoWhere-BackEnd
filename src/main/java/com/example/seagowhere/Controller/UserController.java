package com.example.seagowhere.Controller;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Users;
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
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Integer userId) {
        Users users = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@Valid @RequestBody Users users) {
        Users newUsers = userService.createUser(users);
        return new ResponseEntity<>(newUsers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@Valid @PathVariable(value = "id") Integer userId,
                                            @RequestBody Users usersDetails) {
        Users updatedUsers = userService.updateUser(userId, usersDetails);
        return new ResponseEntity<>(updatedUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Integer userId) {
        Users checkUsers = userService.getUserById(userId).map(user -> {
            userService.deleteUser(user.getId());
            return user;
        }).orElseThrow(() -> new ResourceNotFoundException());

        String response = String.format("Users %s deleted successfully", checkUsers.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Users> getUserByEmail(@RequestParam(value = "email") String email) {
        Users users = userService.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}