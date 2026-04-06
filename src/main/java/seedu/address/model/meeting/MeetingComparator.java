package seedu.address.model.meeting;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Comparator for Meeting in Meeting List.
 * Meetings will be sorted by date (earliest first).
 */
public class MeetingComparator {
    public static final Comparator<Meeting> UPCOMING_THEN_PAST_ASCENDING_DATE =
            // Group upcoming meetings first, then sort by date ascending.
            Comparator.comparing((Meeting m) -> m.isBefore(LocalDate.now())).thenComparing(Meeting::getDate);
}
