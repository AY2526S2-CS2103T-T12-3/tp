package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.ID_1;
import static seedu.address.testutil.TypicalPersons.ID_3;
import static seedu.address.testutil.TypicalPersons.ID_4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seedu.address.model.meeting.Meeting;

/**
 * A utility class containing a list of {@code Meeting} objects to be used in tests.
 */
public class TypicalMeetings {
    public static final Meeting PROJECT_MEETING = new MeetingBuilder()
            .withDescription("Project Meeting")
            .withDate("2026-06-15")
            .withParticipants(Set.of(ID_1, ID_3, ID_4))
            .build();

    public static final Meeting COFFEE_MEETING = new MeetingBuilder()
            .withDescription("Coffee")
            .withDate("2026-06-11")
            .withParticipants(Set.of(ID_1))
            .build();
    public static final Meeting STRATEGY_MEETING = new MeetingBuilder()
            .withDescription("Strategy Discussion")
            .withDate("2026-07-15")
            .withParticipants(Set.of(ID_1, ID_3))
            .build();

    private TypicalMeetings() {} // prevents instantiation

    public static List<Meeting> getTypicalMeetings() {
        return new ArrayList<>((Arrays.asList(
                PROJECT_MEETING,
                COFFEE_MEETING,
                STRATEGY_MEETING)));
    }
}
