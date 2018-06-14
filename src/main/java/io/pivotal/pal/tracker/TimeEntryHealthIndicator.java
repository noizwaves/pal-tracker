package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    private static final int MAX_TIME_ENTRIES = 5;
    private final TimeEntryRepository repository;

    public TimeEntryHealthIndicator(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        if (repository.list().size() < MAX_TIME_ENTRIES) {
            return Health.up().build();
        } else {
            return Health.down().build();
        }
    }
}
