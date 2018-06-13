package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private final Map<Long, TimeEntry> items;
    private Long nextId;

    public InMemoryTimeEntryRepository() {
        nextId = 1L;
        items = new HashMap<>();
    }

    public TimeEntry create(TimeEntry toCreate) {
        TimeEntry newItem = new TimeEntry(
                nextId++,
                toCreate.getProjectId(),
                toCreate.getUserId(),
                toCreate.getDate(),
                toCreate.getHours()
        );

        items.put(newItem.getId(), newItem);

        return newItem;
    }

    public TimeEntry find(Long id) {
        return items.get(id);
    }

    public List<TimeEntry> list() {
        return items.values().stream().collect(Collectors.toList());
    }

    public TimeEntry update(Long id, TimeEntry updated) {
        TimeEntry newItem = new TimeEntry(
                id,
                updated.getProjectId(),
                updated.getUserId(),
                updated.getDate(),
                updated.getHours()
        );

        items.put(newItem.getId(), newItem);

        return newItem;

    }

    public void delete(Long id) {
        items.remove(id);
    }


}
