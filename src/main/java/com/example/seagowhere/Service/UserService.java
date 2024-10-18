package com.example.seagowhere.Service;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Users;
import com.example.seagowhere.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public Users createUser(Users users) {
        return userRepository.save(users);
    }

    public Users updateUser(Integer userId, Users usersDetails) {
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(usersDetails.getFirstName());
            user.setLastName(usersDetails.getLastName());
            user.setEmail(usersDetails.getEmail());
            user.setPassword(usersDetails.getPassword());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Users not found with id " + userId));
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Users not found with email: " + username));
    }
}