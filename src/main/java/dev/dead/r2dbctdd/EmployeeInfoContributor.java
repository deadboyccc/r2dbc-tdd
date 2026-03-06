package dev.dead.r2dbctdd;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeInfoContributor implements InfoContributor {
    private final EmployeeRepository repository;

    public EmployeeInfoContributor(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void contribute(Info.@NonNull Builder builder) {
        repository.count()
                .subscribe(count -> builder.withDetail("employeeCount", count));
    }

}
