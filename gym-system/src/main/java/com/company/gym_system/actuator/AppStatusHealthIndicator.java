package com.company.gym_system.actuator;

import com.company.gym_system.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppStatusHealthIndicator implements HealthIndicator {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public Health health() {
        try {
            long types = trainingTypeRepository.count();
            log.debug("AppStatusHealthIndicator: trainingTypes.count={}", types);
            if (types > 0) {
                return Health.up().withDetail("trainingTypes.count", types).build();
            } else {
                return Health.status("DEGRADED")
                        .withDetail("reason", "No training types configured")
                        .withDetail("trainingTypes.count", types)
                        .build();
            }
        } catch (Exception ex) {
            log.error("AppStatusHealthIndicator error", ex);
            return Health.down(ex).build();
        }
    }
}
