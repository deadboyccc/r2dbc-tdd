package dev.dead.r2dbctdd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ActuatorProbes {
    @Bean
    public ApplicationRunner applicationRunner(
            ApplicationContext applicationContext) {
        return args -> {
            AvailabilityChangeEvent.publish(applicationContext, ReadinessState.REFUSING_TRAFFIC);
            log.debug("Application started");

            Thread.sleep(12_000);
            log.debug("Application stopped");
        };
    }
}
