package dev.dead.r2dbctdd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmployeeBootstrap implements CommandLineRunner {

    private final EmployeeRepository repository;
    private final Random random = new Random();

    @Scheduled(fixedRate = 8000)
    public void addAnotherEmployee() {
        EmployeeEntity.Status[] statuses = EmployeeEntity.Status.values();
        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .firstName("First" + random.nextInt(1000))
                .lastName("Last" + random.nextInt(1000))
                .status(statuses[random.nextInt(statuses.length)])
                .build();

        repository.save(newEmployee)
                .subscribe(
                        employee -> log.info("Added new employee: {}", employee),
                        error -> log.error("Error adding employee: ", error)
                );
    }

    @Override
    public void run(String... args) {
        repository.count()
                .filter(count -> count == 0) // Only run if the table is empty
                .flatMapMany(count -> {
                    log.info("Generating 150 employees...");
                    return Flux.range(1, 150)
                            .map(this::createEmployee)
                            .collectList()
                            .flatMapMany(repository::saveAll);
                })
                .subscribe(
                        employee -> {
                        },
                        error -> log.error("Error bootstrapping data: ", error),
                        () -> log.info("Successfully bootstrapped 150 employees.")
                );
    }

    private EmployeeEntity createEmployee(Integer index) {
        EmployeeEntity.Status[] statuses = EmployeeEntity.Status.values();

        return EmployeeEntity.builder()
                .firstName("First" + index)
                .lastName("Last" + index)
                .status(statuses[random.nextInt(statuses.length)])
                .build();
    }
}
