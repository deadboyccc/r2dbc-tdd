package dev.dead.r2dbctdd;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class WackoHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int hour = LocalTime.now()
                .getHour();

        // 1. After lunch logic
        if (hour > 12) {
            return Health.outOfService()
                    .withDetail("reason", "I'm out of service after lunchtime")
                    .withDetail("hour", hour)
                    .build();
        }

        // 2. Random 10% failure logic
        if (Math.random() <= 0.1) {
            return Health.down()
                    .withDetail("reason", "I break 10% of the time")
                    .build();
        }

        // 3. Healthy state
        return Health.up()
                .withDetail("reason", "All is good!")
                .withDetail("hour", hour)
                .build();
    }
}
