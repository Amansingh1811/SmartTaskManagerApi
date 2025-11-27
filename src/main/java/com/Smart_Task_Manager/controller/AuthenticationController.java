package com.Smart_Task_Manager.controller;

import com.Smart_Task_Manager.dto.LoginRequest;
import com.Smart_Task_Manager.dto.Users;
import com.Smart_Task_Manager.repository.UserRepository;
import com.Smart_Task_Manager.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    Logger detailedLogger = LoggerFactory.getLogger("detailedLogger");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {

        detailedLogger.info("Attempting login for email: {}", loginRequest.email());
        String token = authenticationService.login(loginRequest.email(), loginRequest.password());
        detailedLogger.info("Login successful for email: {}", loginRequest.email());
        Users user = userRepository.findByEmail(loginRequest.email()).orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("name", user.getName());

        return response;
    }
}

