package seedu.address.model.meeting;

import java.util.Comparator;

/**
 * Comparator for Meeting in Meeting List.
 * Meetings will be sorted by date (earliest first).
 */
public class MeetingComparator {
    public static final Comparator<Meeting> EARLIEST_DATE_FIRST =
            Comparator.comparing(meeting -> meeting.getDate().date);
}
