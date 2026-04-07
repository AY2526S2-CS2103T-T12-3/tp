package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEPARATOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collection;
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
 * Edits the details of an existing person in the contact list.
 */
public class AddTagCommand extends Command {

    public static final String COMMAND_WORD = "addtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds tags to one or more persons identified by their indices.\n"
            + "Multiple persons can be specified using commas, and multiple tags using '/'.\n"
            + "Format: " + COMMAND_WORD + " INDEX (must be a positive integer) [,INDEX]... "
            + PREFIX_SEPARATOR + "TAG [" + PREFIX_SEPARATOR + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1" + PREFIX_COMMA + "2 "
            + PREFIX_SEPARATOR + "Friend "
            + PREFIX_SEPARATOR + "Close";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Added the following tags: %1$s.";
    public static final String MESSAGE_NO_TAGS = "At least one tag must be provided.";

    private final Set<Index> targetIndices;
    private final Set<Tag> tags;

    /**
     * Creates an AddTagCommand to add the specified {@code Tag}s to the specified {@code Person}s.
     *
     * @param targetIndices Indices of the persons in the current contact list to add tags to.
     * @param tags The collection of tags to be added.
     */
    public AddTagCommand(Collection<Index> targetIndices, Collection<Tag> tags) {
        requireAllNonNull(targetIndices, tags);

        this.targetIndices = new HashSet<>(targetIndices);
        this.tags = new HashSet<>(tags);
    }

    /**
     * Convenience wrapper for {@code AddTagCommand(Collection<Index>, Collection<Tag>)} that wraps the {@code index}
     * in a set.
     * @param index of the person in the filtered person list to add tags to
     * @param tags the collection of tags to be added
     */
    public AddTagCommand(Index index, Collection<Tag> tags) {
        requireAllNonNull(index, tags);

        this.targetIndices = new HashSet<>();
        this.targetIndices.add(index);
        this.tags = new HashSet<>(tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (tags.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS);
        }

        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // First checks if all indices are valid. If at least 1 is invalid, cancel the operation.
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Snapshot all target persons BEFORE any edits
        List<Person> personsToEdit = new ArrayList<>();
        for (Index index : targetIndices) {
            personsToEdit.add(lastShownList.get(index.getZeroBased()));
        }

        // Add the tags to each specified person object using the snapshotted list
        for (Person personToEdit : personsToEdit) {
            Person editedPerson = createPersonWithAddedTags(personToEdit, tags);
            model.setPerson(personToEdit, editedPerson);
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_ADD_TAG_SUCCESS, tags));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * with additional tags from {@code tags}.
     */
    private static Person createPersonWithAddedTags(Person personToEdit, Collection<Tag> tags) {
        assert personToEdit != null;

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tags);

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
        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand otherAddTagCommand = (AddTagCommand) other;
        return targetIndices.equals(otherAddTagCommand.targetIndices) && tags.equals(otherAddTagCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .add("tags", tags)
                .toString();
    }
}
