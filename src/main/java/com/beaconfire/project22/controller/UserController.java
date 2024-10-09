package com.beaconfire.project22.controller;


import com.beaconfire.project22.Dto.JwtResponse;
import com.beaconfire.project22.Dto.LoginRequest;
import com.beaconfire.project22.JwtUtil.JwtProvider;
import com.beaconfire.project22.Model.Users;
import com.beaconfire.project22.Service.UserService;
import com.sun.tools.jconsole.JConsoleContext;
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

    @Autowired
    private JwtProvider jwtProvider;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Users user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            if (user != null) {
                String token = jwtProvider.generateToken(user.getUsername(), user.getRole());
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping ("/findUserId")
    public  ResponseEntity<Long>  findUserByUsername(@RequestParam String username) {
        Users user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user.getUserId());
    }
}
