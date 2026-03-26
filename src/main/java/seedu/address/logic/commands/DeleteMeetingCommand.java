package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.MeetingUtil.removeMeetingFromAllParticipants;
import static seedu.address.logic.commands.MeetingUtil.validateMeetingIndices;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;

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
            "Deleted meeting(s) %1$s";

    public static final String MESSAGE_INVALID_MEETING_INDEX =
            "Invalid meeting index provided: %1$s";

    private final Set<Index> meetingIndices;

    /**
     * Creates an DeleteMeetingCommand to delete the specified {@code Meeting}s
     *
     * @param meetingIndices The indices of the meetings to delete in the persons
     */
    public DeleteMeetingCommand(Set<Index> meetingIndices) {
        requireNonNull(meetingIndices);

        this.meetingIndices = new HashSet<>(meetingIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Get the filtered list of meetings directly
        List<Meeting> lastShownMeetingList = model.getFilteredMeetingList();

        // Validate meeting indices first
        validateMeetingIndices(lastShownMeetingList, meetingIndices);

        // Track meetings actually deleted for feedback
        Set<Meeting> meetingsToDelete = new HashSet<>();
        Set<Index> actualDeletedIndices = new HashSet<>();

        for (Index meetingIdx : meetingIndices) {
            int zeroBased = meetingIdx.getZeroBased();
            if (zeroBased >= 0 && zeroBased < lastShownMeetingList.size()) {
                meetingsToDelete.add(lastShownMeetingList.get(zeroBased));
                actualDeletedIndices.add(meetingIdx);
            }
        }

        if (meetingsToDelete.isEmpty()) {
            return new CommandResult(MESSAGE_INVALID_MEETING_INDEX);
        }

        // Delete meetings from model
        for (Meeting meeting : meetingsToDelete) {
            removeMeetingFromAllParticipants(meeting, model);
        }

        // Format only the indices of meetings actually deleted
        String meetingIndexString = formatMeetingIndices(actualDeletedIndices);
        return new CommandResult(
                String.format(MESSAGE_DELETE_MEETING_SUCCESS, meetingIndexString));
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

        return meetingIndices.equals(otherCommand.meetingIndices);
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
