package com.company.gym_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectionService {
    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> blockedUntil = new ConcurrentHashMap<>();

    @Value("${spring.security.bruteForce.maxAttempts:3}")
    private int maxAttempts;

    @Value("${spring.security.bruteForce.blockDurationMs:300000}")
    private long blockDurationMs;

    public void loginFailed(String username) {
        int currentAttempts = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, currentAttempts);
        if (currentAttempts >= maxAttempts) {
            blockedUntil.put(username, System.currentTimeMillis() + blockDurationMs);
        }
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
        blockedUntil.remove(username);
    }

    public boolean isBlocked(String username) {
        Long until = blockedUntil.get(username);
        if (until == null) return false;
        if (System.currentTimeMillis() > until) {
            blockedUntil.remove(username);
            attempts.remove(username);
            return false;
        }
        return true;
    }
}
