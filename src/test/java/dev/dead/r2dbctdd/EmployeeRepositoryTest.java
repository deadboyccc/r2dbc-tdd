package dev.dead.r2dbctdd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataR2dbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repo;

    @BeforeEach
    void setUp() {
        var employees = Flux.just(
                EmployeeEntity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .status(EmployeeEntity.Status.ONSITE)
                        .build(),
                EmployeeEntity.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .status(EmployeeEntity.Status.REMOTE)
                        .build(),
                EmployeeEntity.builder()
                        .firstName("Ahmed")
                        .lastName("Baghdad")
                        .status(EmployeeEntity.Status.ONSITE)
                        .build()
        );

        var setup = repo.deleteAll()
                .thenMany(repo.saveAll(employees));

        StepVerifier.create(setup)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void shouldFindAllEmployees() {
        StepVerifier.create(repo.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        // Managed by @DirtiesContext
    }
}
