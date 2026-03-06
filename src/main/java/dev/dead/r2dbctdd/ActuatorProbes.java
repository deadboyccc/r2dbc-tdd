package dev.dead.r2dbctdd;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorProbes {
    @Bean
    public ApplicationRunner applicationRunner(
            ApplicationContext applicationContext) {
        return args -> {
            AvailabilityChangeEvent.publish(applicationContext, ReadinessState.REFUSING_TRAFFIC);

            Thread.sleep(80000);
        };
    }
}
