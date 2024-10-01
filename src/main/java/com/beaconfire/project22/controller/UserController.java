package com.beaconfire.project22.controller;


import com.beaconfire.project22.Dto.LoginRequest;
import com.beaconfire.project22.Model.Users;
import com.beaconfire.project22.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody Users user
            , BindingResult result){
        if (result.hasErrors()) {
            String errorMessage = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body("Validation failed: " + errorMessage);
        }

        try {
            userService.registerUser(user);
            return  ResponseEntity.ok("User registered successfully.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
