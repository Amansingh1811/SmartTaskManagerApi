package com.Smart_Task_Manager.service;

import com.Smart_Task_Manager.security.JWTTokenProvider;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    Logger detailedLogger = org.slf4j.LoggerFactory.getLogger("detailedLogger");

    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jWTTokenProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTTokenProvider jWTTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jWTTokenProvider = jWTTokenProvider;
    }

    public String login(String email, String password) {
        // Implement login logic here
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        detailedLogger.info("User {} authenticated successfully.", auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return jWTTokenProvider.generateToken(userDetails);
    }
}
