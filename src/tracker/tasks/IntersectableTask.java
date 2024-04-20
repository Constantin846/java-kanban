package tracker.tasks;

import java.time.ZonedDateTime;

public interface IntersectableTask {
    ZonedDateTime getStartTime();

    ZonedDateTime getEndTime();
}
