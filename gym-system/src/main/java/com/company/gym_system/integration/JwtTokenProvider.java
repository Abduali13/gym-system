package com.company.gym_system.integration;

import com.company.gym_system.util.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateServiceToken() {
        // In a real setup, subject could be service name or client id
        return jwtUtil.generateToken("gym-system");
    }
}
