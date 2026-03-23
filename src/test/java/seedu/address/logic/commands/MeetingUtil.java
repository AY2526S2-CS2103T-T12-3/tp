package seedu.address.logic.commands;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * Helper class for meeting related methods
 */
public class MeetingUtil {
    /** Helper to return a new Person object with the meeting added. */
    public static Person addMeetingToPerson(Person person, String description, LocalDate date) {
        Set<Meeting> updatedMeetings = new HashSet<>(person.getMeetings());
        updatedMeetings.add(new Meeting(description, date));
        return new Person(person.getId(),
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getTags(),
                updatedMeetings);
    }
}
