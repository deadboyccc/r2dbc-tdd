package dev.dead.r2dbctdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;


@DataR2dbcTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repo;

    @BeforeEach
    void setUp() {
        var employees = List.of(
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

        // Chain deletion and insertion into one pipeline
        var setup = repo.deleteAll()
                .thenMany(repo.saveAll(employees))
                .collectList();

        StepVerifier.create(setup)
                .expectNextCount(1) // One list of 3 employees
                .verifyComplete();
    }

    @Test
    void shouldFindAllEmployees() {
        StepVerifier.create(repo.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void shouldFindEmployeeById() {
        // FIX: Fetch an actual ID from the DB first to avoid sequence issues
        repo.findAll()
                .filter(e -> e.getFirstName()
                        .equals("John"))
                .next() // Get the first John
                .flatMap(john -> repo.findById(john.getId()))
                .as(StepVerifier::create)
                .expectNextMatches(employee -> employee.getFirstName()
                        .equals("John"))
                .verifyComplete();
    }

    @Test
    void chainingMapsAndFlatmap() {
        // Using StepVerifier instead of Thread.sleep()
        var employee = EmployeeEntity.builder()
                .firstName("Joe")
                .lastName("Biden")
                .status(EmployeeEntity.Status.ONSITE)
                .build();

        repo.save(employee)
                .flatMapMany(saved -> repo.findAll()
                        .map(EmployeeEntity::getFirstName)
                        .filter(name -> name.contains("a"))
                        .thenMany(Flux.just(saved)))
                .as(StepVerifier::create)
                .expectNextMatches(e -> e.getFirstName()
                        .equals("Joe"))
                .verifyComplete();
    }

    @Test
    void magicStuff() {
        Mono<Integer> integerMono = Mono.just(1);

        // Flatmap naturally flattens nested Monos
        integerMono.map(Mono::just)
                .flatMap(inner -> inner)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }
}
