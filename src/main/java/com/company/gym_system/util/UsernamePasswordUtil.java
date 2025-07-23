package com.company.gym_system.util;

import com.company.gym_system.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

public final class UsernamePasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateUsername(String firstName, String lastName, Map<String, Object> existingEntries
    ) {
        String baseUsername = firstName + "." + lastName;
        int count = 1;
        String username = baseUsername;

        while (existingEntries.containsKey(username)) {
            username = baseUsername + count++;
        }

        return username;
    }

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return password.toString();
    }

}
