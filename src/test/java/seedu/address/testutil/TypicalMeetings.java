package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.UUID_1;
import static seedu.address.testutil.TypicalPersons.UUID_3;
import static seedu.address.testutil.TypicalPersons.UUID_4;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seedu.address.model.meeting.Meeting;

/**
 * A utility class containing a list of {@code Meeting} objects to be used in tests.
 */
public class TypicalMeetings {
    public static final Meeting COFFEE_MEETING = new Meeting(
            "Coffee",
            LocalDate.of(2026, 6, 11),
            Set.of(UUID_1));
    public static final Meeting PROJECT_MEETING = new Meeting(
            "Project Meeting",
            LocalDate.of(2026, 6, 15),
            Set.of(UUID_1, UUID_3, UUID_4));
    public static final Meeting STRATEGY_MEETING = new Meeting(
            "Strategy Discussion",
            LocalDate.of(2026, 7, 15),
            Set.of(UUID_1, UUID_3)
    );

    private TypicalMeetings() {} // prevents instantiation

    public static List<Meeting> getTypicalMeetings() {
        return new ArrayList<>((Arrays.asList(
                PROJECT_MEETING,
                COFFEE_MEETING,
                STRATEGY_MEETING)));
    }
}
