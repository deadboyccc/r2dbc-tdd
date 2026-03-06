package dev.dead.r2dbctdd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.relational.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@ManagedResource
public class EmployeeCounterMBean {

    private final EmployeeRepository employeeRepository;
    private final AtomicLong counter = new AtomicLong(-1);

    public EmployeeCounterMBean(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @EventListener
    public void afterSave(AfterSaveEvent<EmployeeEntity> event) {
        log.info("After save event: {}", event);
        getEmployeeCount();
        counter.incrementAndGet();
    }

    @EventListener
    public long afterDelete(AfterDeleteEvent<EmployeeEntity> event) {
        log.info("After delete event: {}", event);
        getEmployeeCount();
        return counter.decrementAndGet();
    }

    @ManagedAttribute
    public long getEmployeeCount() {
        if (counter.get() == -1) {
            // Block just once to initialize from DB
            Long count = employeeRepository.count()
                    .block();
            counter.compareAndSet(-1, count != null ? count : 0);
        }
        return counter.get();
    }

    @ManagedOperation
    public long resetCounter(long delta) {
        return counter.addAndGet(delta);
    }
}
