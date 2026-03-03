package dev.dead.r2dbctdd;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<EmployeeEntity, Long> {
}
