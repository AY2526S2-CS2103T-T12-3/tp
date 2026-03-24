package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.MeetingUtil.createPersonWithMeetingsRemoved;
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

    public static final String MESSAGE_INVALID_PERSON_INDEX =
            "Invalid person index provided.";

    public static final String MESSAGE_INVALID_MEETING_INDEX =
            "Invalid meeting index provided.";

    private final Set<Index> personIndices;
    private final Set<Index> meetingIndices;

    public DeleteMeetingCommand(Set<Index> personIndices, Set<Index> meetingIndices) {
        requireNonNull(personIndices);
        requireNonNull(meetingIndices);

        this.personIndices = new HashSet<>(personIndices);
        this.meetingIndices = new HashSet<>(meetingIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        List<String> updatedPersonNames = new ArrayList<>();

        // Validate all person indices first
        for (Index personIndex : personIndices) {
            if (personIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
            }
        }

        for (Index personIndex : personIndices) {
            Person personToEdit = lastShownList.get(personIndex.getZeroBased());
            Set<Meeting> meetings = personToEdit.getMeetings();

            // Validate meeting indices for THIS person
            for (Index meetingIndex : meetingIndices) {
                if (meetingIndex.getZeroBased() >= meetings.size()) {
                    throw new CommandException(MESSAGE_INVALID_MEETING_INDEX);
                }
            }

            // Remove meetings (descending to avoid shifting bug)
            List<Integer> indicesToRemove = meetingIndices.stream()
                    .map(Index::getZeroBased)
                    .sorted(Comparator.reverseOrder())
                    .toList();

            for (int idx : indicesToRemove) {
                meetings.remove(idx);
            }

            Person updatedPerson = createPersonWithMeetingsRemoved(personToEdit, meetings);

            model.setPerson(personToEdit, updatedPerson);
            updatedPersonNames.add(personToEdit.getName().fullName);
        }

        String meetingIndexString = meetingIndices.stream()
                .map(i -> String.valueOf(i.getOneBased()))
                .collect(Collectors.joining(", "));

        return new CommandResult(
                String.format(MESSAGE_DELETE_MEETING_SUCCESS,
                        meetingIndexString,
                        String.join(", ", updatedPersonNames))
        );
    }
}
