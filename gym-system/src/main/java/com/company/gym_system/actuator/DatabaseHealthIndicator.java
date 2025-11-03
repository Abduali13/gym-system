package com.company.gym_system.actuator;

import com.company.gym_system.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final TrainingRepository trainingRepository;

    @Override
    public Health health() {
        try {
            long count = trainingRepository.count();
            log.debug("DatabaseHealthIndicator: training count={}", count);
            return Health.up()
                    .withDetail("training.count", count)
                    .build();
        } catch (Exception ex) {
            log.error("DatabaseHealthIndicator error", ex);
            return Health.down(ex).build();
        }
    }
}
