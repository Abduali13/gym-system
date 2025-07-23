package com.company.gym_system.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilsTest {

    @Test
    @DisplayName("Should get ID from entity with getId method")
    void getId_ShouldGetIdFromEntity_WhenEntityHasGetIdMethod() {
        // Arrange
        TestEntity entity = new TestEntity(1L);

        // Act
        Long id = ReflectionUtils.getId(entity);

        // Assert
        assertEquals(1L, id);
    }

    @Test
    @DisplayName("Should throw IllegalStateException when entity has no getId method")
    void getId_ShouldThrowIllegalStateException_WhenEntityHasNoGetIdMethod() {
        // Arrange
        EntityWithoutId entity = new EntityWithoutId();

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ReflectionUtils.getId(entity);
        });
        assertTrue(exception.getMessage().contains("Failed to retrieve id from EntityWithoutId"));
    }

    @Test
    @DisplayName("Should throw NullPointerException when entity is null")
    void getId_ShouldThrowNullPointerException_WhenEntityIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            ReflectionUtils.getId(null);
        });
    }

    // Test entity class with getId method
    private static class TestEntity {
        private final Long id;

        public TestEntity(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    // Test entity class without getId method
    private static class EntityWithoutId {
        private final Long someId = 1L;

        public Long getSomeId() {
            return someId;
        }
    }
}