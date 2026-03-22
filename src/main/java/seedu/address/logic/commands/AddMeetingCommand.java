package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * Adds a meeting to one or more persons in the address book.
 */
public class AddMeetingCommand extends Command {

    public static final String COMMAND_WORD = "addmeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a meeting to the specified person(s) "
            + "by index.\n"
            + "Parameters: i/INDEX (must be a positive integer) "
            + "[" + PREFIX_COMMA + "INDEX]... "
            + "d/DESCRIPTION "
            + "dt/DATE (must be YYYY-MM-DD)\n"
            + "Example: " + COMMAND_WORD + " i/1,2 d/Project discussion dt/2026-03-25";

    public static final String MESSAGE_ADD_MEETING_SUCCESS = "Added meeting to person(s): %1$s";
    public static final String MESSAGE_INVALID_PERSON_INDEX = "Invalid person index provided.";
    public static final String MESSAGE_INVALID_DATE_FORMAT = "Invalid date format! Use YYYY-MM-DD.";

    private final List<Index> indices;
    private final String description;
    private final LocalDate date;

    /**
     * @param indices Indexes of persons to add the meeting to
     * @param description Description of the meeting
     * @param date Date of the meeting (YYYY-MM-DD)
     */
    public AddMeetingCommand(Set<Index> indices, String description, LocalDate date) {
        requireNonNull(indices);
        requireNonNull(description);
        requireNonNull(date);

        this.indices = indices.stream()
                .sorted((i1, i2) -> Integer.compare(i1.getZeroBased(), i2.getZeroBased()))
                .toList();
        this.description = description;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        List<String> updatedPersonNames = new ArrayList<>();

        // Loop through each index and add the meeting
        for (Index index : indices) {

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
            }

            Person personToEdit = lastShownList.get(index.getZeroBased());

            // Create the meeting
            Meeting meeting = new Meeting(description, date);
            Set<Meeting> copiedMeetings = new HashSet<>(personToEdit.getMeetings());

            // Now you can safely add a new meeting
            copiedMeetings.add(meeting);

            // Add meeting to person
            Person updatedPerson = new Person(
                    personToEdit.getName(),
                    personToEdit.getPhone(),
                    personToEdit.getEmail(),
                    personToEdit.getTags(),
                    copiedMeetings
            );

            // Update model
            model.setPerson(personToEdit, updatedPerson);

            updatedPersonNames.add(personToEdit.getName().fullName);
        }

        return new CommandResult(String.format(MESSAGE_ADD_MEETING_SUCCESS, String.join(", ", updatedPersonNames)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddMeetingCommand)) {
            return false;
        }

        AddMeetingCommand otherCommand = (AddMeetingCommand) other;
        return indices.equals(otherCommand.indices)
                && description.equals(otherCommand.description)
                && date.equals(otherCommand.date);
    }
}
