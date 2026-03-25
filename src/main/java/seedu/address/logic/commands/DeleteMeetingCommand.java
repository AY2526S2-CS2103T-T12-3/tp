package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.MeetingUtil.removeMeetingFromAllParticipants;
import static seedu.address.logic.commands.MeetingUtil.validatePersonIndices;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * For the persons at the given indices, deletes the meetings at the specified meeting indices.
 * Each deleted meeting is removed from every participant linked to that meeting.
 */
public class DeleteMeetingCommand extends Command {
    public static final String COMMAND_WORD = "deletemeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes meeting(s) from the specified person(s) by index.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) "
            + "[" + PREFIX_COMMA + "PERSON_INDEX]... "
            + "m/MEETING_INDEX (must be a positive integer) "
            + "[" + PREFIX_COMMA + "MEETING_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " 1,2 m/1,3";

    public static final String MESSAGE_DELETE_MEETING_SUCCESS =
            "Deleted meeting(s) %1$s from person(s): %2$s";

    public static final String MESSAGE_INVALID_MEETING_INDEX =
            "Invalid meeting index provided.";

    private final List<Index> personIndices;
    private final Set<Index> meetingIndices;

    /**
     * Creates an DeleteMeetingCommand to delete the specified {@code Meeting}s
     *
     * @param personIndices Indexes of persons to look for the meetings to delete
     * @param meetingIndices The indices of the meetings to delete in the persons
     */
    public DeleteMeetingCommand(Set<Index> personIndices, Set<Index> meetingIndices) {
        requireNonNull(personIndices);
        requireNonNull(meetingIndices);

        this.personIndices = personIndices.stream()
                .sorted((i1, i2) -> Integer.compare(i1.getZeroBased(), i2.getZeroBased()))
                .toList();
        this.meetingIndices = new HashSet<>(meetingIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        List<String> updatedPersonNames = new ArrayList<>();

        // Validate person indices first
        validatePersonIndices(lastShownList, personIndices);

        // Track if we actually deleted at least one meeting
        boolean anyDeleted = false;
        Set<Meeting> meetingsToDelete = new HashSet<>();

        for (Index personIndex : personIndices) {
            Person person = lastShownList.get(personIndex.getZeroBased());
            List<Meeting> personMeetings = new ArrayList<>(person.getMeetings());
            personMeetings.sort(Comparator.comparing(Meeting::getDate)
                    .thenComparing(Meeting::getDescription));

            for (Index meetingIdx : meetingIndices) {
                if (meetingIdx.getZeroBased() < personMeetings.size()) {
                    meetingsToDelete.add(personMeetings.get(meetingIdx.getZeroBased()));
                    anyDeleted = true;
                }
            }

            // Add person to updated list only if they had at least one meeting to delete
            if (!meetingsToDelete.isEmpty()) {
                updatedPersonNames.add(person.getName().fullName);
            }
        }

        // Only delete if we found at least one valid meeting
        if (anyDeleted) {
            for (Meeting meeting : meetingsToDelete) {
                removeMeetingFromAllParticipants(lastShownList, meeting, model);
            }

            String meetingIndexString = formatMeetingIndices(meetingIndices);
            return new CommandResult(
                    String.format(MESSAGE_DELETE_MEETING_SUCCESS,
                            meetingIndexString,
                            String.join(", ", updatedPersonNames))
            );
        } else {
            return new CommandResult(MESSAGE_INVALID_MEETING_INDEX);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteMeetingCommand)) {
            return false;
        }

        DeleteMeetingCommand otherCommand = (DeleteMeetingCommand) other;

        return personIndices.equals(otherCommand.personIndices)
                && meetingIndices.equals(otherCommand.meetingIndices);
    }

    /**
     * Formats the meeting indices into a string to be used for {@code MESSAGE_DELETE_MEETING_SUCCESS}.
     */
    private String formatMeetingIndices(Set<Index> meetingIndices) {
        return meetingIndices.stream()
                .map(i -> String.valueOf(i.getOneBased()))
                .collect(Collectors.joining(", "));
    }
}
