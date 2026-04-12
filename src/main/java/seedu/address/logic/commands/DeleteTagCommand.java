package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEPARATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes the specified tags (case-insensitive) from the persons identified
 * using their indices from the displayed contact list.
 */
public class DeleteTagCommand extends Command {
    public static final String COMMAND_WORD = "deletetag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes tag(s) (case-insensitive) from one or more persons identified "
            + "by their indices in the displayed contact list.\n"
            + "Format: " + COMMAND_WORD + " INDEX [,INDEX]... "
            + PREFIX_SEPARATOR + "TAG [" + PREFIX_SEPARATOR + "TAG]...\n"
            + "Note: INDEX must be a positive integer\n"
            + "Example: " + COMMAND_WORD + " 1,2 "
            + PREFIX_SEPARATOR + "friend "
            + PREFIX_SEPARATOR + "colleague";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Removed tags %1$s from specified persons";
    public static final String MESSAGE_NO_TAGS = "At least one tag must be provided.";
    public static final String MESSAGE_NO_VALID_TAGS =
            "Error: None of the specified tags exist in any of the specified persons.";

    private final Set<Index> targetIndices;
    private final Set<Tag> tags;

    /**
     * Acts as the constructor for DeleteTagCommand.
     *
     * @param targetIndices The target indices representing the persons to be edited.
     * @param tags The tags to be removed from the specified persons.
     */
    public DeleteTagCommand(Set<Index> targetIndices, Set<Tag> tags) {
        this.targetIndices = new HashSet<>(targetIndices);
        this.tags = new HashSet<>(tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (tags.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS);
        }

        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        boolean hasAtLeastOneValidTag = false;

        List<Person> personsToEdit = getPersonsToEdit(lastShownList, targetIndices);

        // delete the tags from each specified person object
        for (Person person : personsToEdit) {
            // edits the tags in each person and sets the edited person
            if (!Collections.disjoint(person.getTags(), tags)) {
                hasAtLeastOneValidTag = true;
            }
            Person editedPerson = createPersonWithDeletedTags(person, tags);
            model.setPerson(person, editedPerson);
        }

        if (!hasAtLeastOneValidTag) {
            throw new CommandException(MESSAGE_NO_VALID_TAGS);
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, tags));
    }

    /**
     * Returns the list of persons to edit and delete tags from.
     *
     * @param lastShownList The currently displayed contact list.
     * @param targetIndices The set of target indices representing persons to be edited.
     * @return The list of persons to edit.
     * @throws CommandException if an invalid person index (i.e. negative, zero, or out of bounds) is provided.
     */
    private static List<Person> getPersonsToEdit(List<Person> lastShownList, Set<Index> targetIndices)
            throws CommandException {
        // checks for any invalid indices
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        List<Person> personsToEdit = new ArrayList<>();
        for (Index index : targetIndices) {
            personsToEdit.add(lastShownList.get(index.getZeroBased()));
        }

        return personsToEdit;
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * without tags from {@code tags}.
     */
    private static Person createPersonWithDeletedTags(Person personToEdit, Collection<Tag> tags) {
        assert personToEdit != null;

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.removeAll(tags);

        return new Person(
                personToEdit.getId(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                updatedTags
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherDeleteTagCommand = (DeleteTagCommand) other;

        return targetIndices.equals(otherDeleteTagCommand.targetIndices) && tags.equals(otherDeleteTagCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .add("tags", tags)
                .toString();
    }
}
