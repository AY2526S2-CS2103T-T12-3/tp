package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            + ": Deletes meeting(s) specified by the indices.\n"
            + "Parameters: MEETING_INDEX (must be a positive integer) "
            + "[" + PREFIX_COMMA + "MEETING_INDEX]... \n"
            + "Example: " + COMMAND_WORD + " 1,2";

    public static final String MESSAGE_DELETE_MEETING_SUCCESS =
            "Deleted meeting(s): %1$s";

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

        // Validate indices and collect meetings
        Set<Meeting> meetingsToDelete = new HashSet<>();

        for (Index index : meetingIndices) {
            if (index.getZeroBased() >= lastShownMeetingList.size()) {
                throw new CommandException(String.format(MESSAGE_INVALID_MEETING_INDEX, index.getOneBased()));
            }
            meetingsToDelete.add(lastShownMeetingList.get(index.getZeroBased()));
        }

        meetingsToDelete.forEach(model::deleteMeeting);

        // Format only the indices of meetings actually deleted
        return new CommandResult(String.format(MESSAGE_DELETE_MEETING_SUCCESS, meetingsToDelete));
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
}
