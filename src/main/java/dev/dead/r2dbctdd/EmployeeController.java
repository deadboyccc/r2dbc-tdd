package dev.dead.r2dbctdd;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/em")
public class EmployeeController {
    private final EmployeeRepository repository;

    @GetMapping()
    Flux<EmployeeEntity> findAll() {
        return repository.findAll()
                .take(5);
    }

    @PostMapping
    Mono<EmployeeEntity> save(@RequestBody EmployeeEntity entity) {
        return repository.save(entity);
    }

    @GetMapping("/{id}")
    Mono<EmployeeEntity> findById(@PathVariable Long id) {
        return repository.findById(id);
    }
}
