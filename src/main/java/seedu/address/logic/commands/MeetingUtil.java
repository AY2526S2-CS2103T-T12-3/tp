package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_INVALID_PERSON_INDEX;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
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
    public static Person createPersonWithGivenMeetings(Person personToEdit, Set<Meeting> updatedMeetings) {
        return new Person(
                personToEdit.getId(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTags(),
                new HashSet<>(updatedMeetings)
        );
    }

    /**
     * Collates all participant's UUID to put into a {@code Meeting} object.
     */
    public static List<UUID> collectParticipantIds(List<Person> participants, List<Index> indices)
            throws CommandException {
        List<UUID> ids = new ArrayList<>();
        for (Index index : indices) {
            if (index.getZeroBased() >= participants.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
            }
            ids.add(participants.get(index.getZeroBased()).getId());
        }
        return ids;
    }

    /**
     * Validates if there are people in the given indices in the AddressBook.
     */
    public static void validatePersonIndices(List<Person> listOfPeople, List<Index> personIndices)
            throws CommandException {

        for (Index personIndex : personIndices) {
            if (personIndex.getZeroBased() >= listOfPeople.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
            }
        }
    }

    /**
     * Removes the specified meeting from all its participants in the list given.
     *
     * @param lastShownList The list of persons to look through.
     * @param meetingToDelete The meeting to remove.
     * @param model The model to update persons in.
     */
    public static void removeMeetingFromAllParticipants(List<Person> lastShownList,
                                                        Meeting meetingToDelete,
                                                        Model model) throws CommandException {
        requireNonNull(lastShownList);
        requireNonNull(meetingToDelete);
        requireNonNull(model);

        for (UUID participantId : meetingToDelete.getParticipantsID()) {
            // Find the participant in the filtered list
            for (Person person : lastShownList) {
                if (person.getId().equals(participantId)) {
                    Set<Meeting> updatedMeetings = new HashSet<>(person.getMeetings());
                    updatedMeetings.remove(meetingToDelete); // remove the exact meeting object

                    Person updatedPerson = createPersonWithGivenMeetings(person, updatedMeetings);
                    model.setPerson(person, updatedPerson);
                }
            }
        }
    }
}
