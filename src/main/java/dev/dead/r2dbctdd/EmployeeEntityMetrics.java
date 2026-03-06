package dev.dead.r2dbctdd;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.data.relational.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMetrics {

    private final Counter savedCounter;
    private final Counter deletedCounter;

    public EmployeeEntityMetrics(MeterRegistry meterRegistry,
                                 EmployeeRepository employeeRepository) {
        this.savedCounter = meterRegistry.counter("employee.saved");
        this.deletedCounter = meterRegistry.counter("employee.deleted");

        // Gauge: always reflects the real-time DB count
        Gauge.builder("employee.count", employeeRepository, repo ->
                        repo.count()
                                .block()
                )
                .register(meterRegistry);
    }

    @EventListener
    public void onAfterSave(AfterSaveEvent<EmployeeEntity> event) {
        savedCounter.increment();
    }

    @EventListener
    public void onAfterDelete(AfterDeleteEvent<EmployeeEntity> event) {
        deletedCounter.increment();
    }
}
