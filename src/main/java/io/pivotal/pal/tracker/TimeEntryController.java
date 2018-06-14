package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private final TimeEntryRepository repository;
    private final CounterService counter;
    private final GaugeService gauge;

    public TimeEntryController(
            TimeEntryRepository timeEntryRepository,
            CounterService counter,
            GaugeService gauge
    ) {
        this.repository = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry toCreate) {
        TimeEntry created = repository.create(toCreate);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", repository.list().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> items = repository.list();
        counter.increment("TimeEntry.listed");

        return ResponseEntity.ok(items);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        TimeEntry found = repository.find(id);

        if (found == null) {
            return ResponseEntity.notFound().build();
        }

        counter.increment("TimeEntry.read");

        return ResponseEntity.ok(found);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long id, @RequestBody TimeEntry toUpdate) {
        TimeEntry updated = repository.update(id, toUpdate);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        counter.increment("TimeEntry.updated");

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        repository.delete(id);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", repository.list().size());

        return ResponseEntity.noContent().build();
    }
}
