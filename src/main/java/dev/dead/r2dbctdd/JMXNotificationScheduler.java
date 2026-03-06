package dev.dead.r2dbctdd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@ManagedResource
public class JMXNotificationScheduler implements NotificationPublisherAware {

    private final EmployeeRepository employeeRepository;
    private final AtomicLong sequenceNumber = new AtomicLong();
    private NotificationPublisher notificationPublisher;

    public JMXNotificationScheduler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void setNotificationPublisher(
            NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @ManagedAttribute
    public long getEmployeeCount() {
        return employeeRepository.count()
                .block();
    }

    @Scheduled(fixedRate = 5000)
    public void sendCountNotification() {
        if (notificationPublisher == null) return;

        long count = getEmployeeCount();
        Notification notification = new Notification(
                "employee.count",
                this,
                sequenceNumber.incrementAndGet(),
                "Current employee count: " + count
        );
        notificationPublisher.sendNotification(notification);
        log.info("JMX notification sent: {} employees", count);
    }
}
