package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * Helper class for meeting related methods
 */
public class MeetingUtil {
    /**
     * Helper method to return a new Person object with the given new meeting added
     */
    public static Person createPersonWithMeetingAdded(Person person, Meeting meeting) {
        Set<Meeting> updatedMeetings = new HashSet<>(person.getMeetings());
        updatedMeetings.add(meeting);
        return new Person(
                person.getId(),
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getTags(),
                updatedMeetings
        );
    }

    /**
     * Helper method to return a new Person object with the new list of meetings given
     */
    public static Person createPersonWithMeetingsRemoved(Person personToEdit, Set<Meeting> updatedMeetings) {
        return new Person(
                personToEdit.getId(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTags(),
                new HashSet<>(updatedMeetings)
        );
    }
}
