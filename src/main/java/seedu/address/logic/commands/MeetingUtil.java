package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_INVALID_PERSON_INDEX;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_MEETING_ALREADY_EXISTS;
import static seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX;

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
     * Helper method to return a new Person object with the given new meeting added.
     *
     * @throws CommandException if the meeting already exists for this person.
     */
    public static Person createPersonWithMeetingAdded(Person person, Meeting meeting) throws CommandException {
        requireNonNull(person);
        requireNonNull(meeting);

        Set<Meeting> updatedMeetings = new HashSet<>(person.getMeetings());

        if (updatedMeetings.contains(meeting)) {
            throw new CommandException(String.format(
                    MESSAGE_MEETING_ALREADY_EXISTS,
                    person.getName().fullName));
        }

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
     * Validates that all meeting indices are within the bounds of the given meeting list.
     *
     * @param meetingList The list of meetings to validate against.
     * @param meetingIndices The set of indices to validate.
     * @throws CommandException if any index is out of bounds.
     */
    public static void validateMeetingIndices(List<Meeting> meetingList, Set<Index> meetingIndices)
            throws CommandException {
        requireNonNull(meetingList);
        requireNonNull(meetingIndices);

        for (Index idx : meetingIndices) {
            if (idx.getZeroBased() < 0 || idx.getZeroBased() >= meetingList.size()) {
                throw new CommandException(String.format(MESSAGE_INVALID_MEETING_INDEX, idx.getOneBased()));
            }
        }
    }

    /**
     * Removes the specified meeting from all its participants.
     *
     * @param meetingToDelete The meeting to remove.
     * @param model The model to update persons in.
     * @throws CommandException if any participant cannot be found in the model.
     */
    public static void removeMeetingFromAllParticipants(Meeting meetingToDelete, Model model)
            throws CommandException {
        requireNonNull(meetingToDelete);
        requireNonNull(model);

        for (UUID participantId : meetingToDelete.getParticipantsID()) {
            Person participant = model.getPerson(participantId);

            Set<Meeting> updatedMeetings = new HashSet<>(participant.getMeetings());
            updatedMeetings.remove(meetingToDelete);

            Person updatedPerson = createPersonWithGivenMeetings(participant, updatedMeetings);
            model.setPerson(participant, updatedPerson);
        }
    }
}
