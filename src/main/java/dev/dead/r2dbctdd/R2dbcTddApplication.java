package dev.dead.r2dbctdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableR2dbcAuditing
@SpringBootApplication
public class R2dbcTddApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcTddApplication.class, args);
    }

}
