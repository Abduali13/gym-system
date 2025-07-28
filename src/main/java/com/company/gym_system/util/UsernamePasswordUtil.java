package com.company.gym_system.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsernamePasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;

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
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return password.toString();
    }

}
