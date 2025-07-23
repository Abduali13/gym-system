package com.company.gym_system.util;

import java.lang.reflect.Method;
import java.util.Objects;

public final class ReflectionUtils {
    private ReflectionUtils() { /* no‐op */ }

    /** 
     * Calls the public getId() method on the given object. 
     * Throws IllegalStateException if no such method or invocation fails.
     */
    public static <T> Long getId(T entity) {
        Objects.requireNonNull(entity, "Entity must not be null");
        try {
            Method getter = entity.getClass().getMethod("getId");
            //            return Objects.toString(idValue, null);
            return (Long) getter.invoke(entity);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "Failed to retrieve id from " + entity.getClass().getSimpleName(), e);
        }
    }
}