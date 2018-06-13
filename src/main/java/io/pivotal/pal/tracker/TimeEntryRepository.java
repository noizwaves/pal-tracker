package io.pivotal.pal.tracker;

import java.util.List;

public interface TimeEntryRepository {
    TimeEntry create(TimeEntry toCreate);
    TimeEntry find(Long id);
    List<TimeEntry> list();
    TimeEntry update(Long id, TimeEntry updated );
    void delete(Long id);
}
