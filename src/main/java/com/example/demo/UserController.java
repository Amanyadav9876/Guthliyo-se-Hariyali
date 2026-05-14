package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    // Register new user (Donor or Volunteer)
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email!");
        }
        return userRepository.save(user);
    }
    
    // Login user
    @PostMapping("/login")
    public User loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmailAndPassword(
            loginRequest.getEmail(), 
            loginRequest.getPassword()
        ).orElseThrow(() -> new RuntimeException("Invalid email or password!"));
        
        return user;
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found!"));
    }
}

class LoginRequest {
    private String email;
    private String password;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
