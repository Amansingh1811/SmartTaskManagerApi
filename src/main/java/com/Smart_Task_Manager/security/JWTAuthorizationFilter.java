package com.Smart_Task_Manager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    Logger detailedLogger = LoggerFactory.getLogger("detailedLogger");

    private final JWTTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JWTAuthorizationFilter(JWTTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        detailedLogger.info("JWT Filter triggered for URL: {}", request.getRequestURI());
        Collections.list(request.getHeaderNames())
                .forEach(h -> detailedLogger.info("Header -> {} : {}", h, request.getHeader(h)));
        String header = request.getHeader("Authorization");
        detailedLogger.info("header : {}", header);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            detailedLogger.info("JWT Token found: {}", token);
            // Here you would typically validate the token and set the authentication in the security context
            // For example:
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                Authentication auth1 = jwtTokenProvider.getAuthentication(token, userDetails);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/static/")
                || path.equals("/")
                || path.equals("/index.html")
                || path.startsWith("/favicon")
                || path.startsWith("/vite.svg")
                || path.startsWith("/manifest");
    }

}
