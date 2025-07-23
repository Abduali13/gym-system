package com.company.gym_system.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UsernamePasswordUtilTest {

    @Test
    @DisplayName("Should generate username from first and last name")
    void generateUsername_ShouldGenerateUsernameFromFirstAndLastName() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Map<String, Object> existingUsernames = new HashMap<>();

        // Act
        String username = UsernamePasswordUtil.generateUsername(firstName, lastName, existingUsernames);

        // Assert
        assertEquals("John.Doe", username);
    }

    @Test
    @DisplayName("Should add number suffix when username already exists")
    void generateUsername_ShouldAddNumberSuffix_WhenUsernameAlreadyExists() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Map<String, Object> existingUsernames = new HashMap<>();
        existingUsernames.put("John.Doe", new Object());

        // Act
        String username = UsernamePasswordUtil.generateUsername(firstName, lastName, existingUsernames);

        // Assert
        assertEquals("John.Doe1", username);
    }

    @Test
    @DisplayName("Should increment number suffix when username with suffix already exists")
    void generateUsername_ShouldIncrementNumberSuffix_WhenUsernameWithSuffixAlreadyExists() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Map<String, Object> existingUsernames = new HashMap<>();
        existingUsernames.put("John.Doe", new Object());
        existingUsernames.put("John.Doe1", new Object());
        existingUsernames.put("John.Doe2", new Object());

        // Act
        String username = UsernamePasswordUtil.generateUsername(firstName, lastName, existingUsernames);

        // Assert
        assertEquals("John.Doe3", username);
    }

    @Test
    @DisplayName("Should generate random password with 10 characters")
    void generateRandomPassword_ShouldGenerateRandomPasswordWith10Characters() {
        // Act
        String password = UsernamePasswordUtil.generateRandomPassword();

        // Assert
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    @DisplayName("Should generate different passwords on consecutive calls")
    void generateRandomPassword_ShouldGenerateDifferentPasswordsOnConsecutiveCalls() {
        // Act
        String password1 = UsernamePasswordUtil.generateRandomPassword();
        String password2 = UsernamePasswordUtil.generateRandomPassword();

        // Assert
        assertNotEquals(password1, password2);
    }
}