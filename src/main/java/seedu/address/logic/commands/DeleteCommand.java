package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes people identified using their indices from the displayed contact list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes person(s) identified by their index number in the displayed contact list.\n"
            + "Format: " + COMMAND_WORD + " INDEX [,INDEX]...\n"
            + "Note: INDEX must be a positive integer\n"
            + "Example: " + COMMAND_WORD + " 1,2";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person(s): %1$s";

    private final Set<Index> targetIndices;

    /**
     * Creates a DeleteCommand to delete the specified {@code Person}s.
     *
     * @param targetIndices Indexes of persons to delete.
     */
    public DeleteCommand(Set<Index> targetIndices) {
        requireNonNull(targetIndices);

        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        List<Index> sortedIndices = targetIndices.stream()
                .sorted(Comparator.comparingInt(Index::getZeroBased)).toList();

        for (Index index : sortedIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        List<Person> peopleToDelete = new ArrayList<>();
        for (Index index : sortedIndices) {
            Person personToDelete = lastShownList.get(index.getZeroBased());
            peopleToDelete.add(personToDelete);
        }

        String deletedPersons = peopleToDelete.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));

        peopleToDelete.forEach(model::deletePerson);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPersons));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
