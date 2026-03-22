package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20260406;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.ui.PersonCard.sortMeetings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.meeting.Meeting;

public class PersonCardTest {

    @Test
    public void getSortedMeetings_unsortedMeetings_returnsSortedList() {
        // Create meetings (out of order)
        Meeting m1 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260406);
        Meeting m2 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260401);
        Meeting m3 = new Meeting(VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325);

        Set<Meeting> meetingSet = new HashSet<>();
        meetingSet.add(m1);
        meetingSet.add(m2);
        meetingSet.add(m3);

        // Call method
        List<Meeting> sorted = sortMeetings(meetingSet);

        // Assert correct order (earliest → latest)
        assertEquals(m3, sorted.get(0));
        assertEquals(m2, sorted.get(1));
        assertEquals(m1, sorted.get(2));
    }
}
