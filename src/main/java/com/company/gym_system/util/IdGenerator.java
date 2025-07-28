package com.company.gym_system.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {
    private static final AtomicLong sequence = new AtomicLong(1);
    public static Long nextId() {
        return sequence.getAndIncrement();
    }
}