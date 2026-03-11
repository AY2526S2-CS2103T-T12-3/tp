package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG_SEPARATOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
 * Edits the details of an existing person in the address book.
 */
public class AddTagCommand extends Command {

    public static final String COMMAND_WORD = "addtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to the person identified "
            + "by the index number used in the displayed person list. "
            + "Multiple tags can be added, separated by a forward slash (/).\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ADD_TAG_SEPARATOR + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ADD_TAG_SEPARATOR + "Friend "
            + PREFIX_ADD_TAG_SEPARATOR + "Close";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Added tags to: %1$s.";
    public static final String MESSAGE_NO_TAGS = "At least one tag must be provided.";

    private final Index index;
    private final Set<Tag> tagSet;

    /**
     * @param index of the person in the filtered person list to add tags to
     * @param tagSet the set of tags to be added
     */
    public AddTagCommand(Index index, Set<Tag> tagSet) {
        requireAllNonNull(index, tagSet);

        this.index = index;
        this.tagSet = tagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (tagSet.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS);
        }

        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createPersonWithAddedTags(personToEdit, tagSet);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * with additional tags from {@code tagSet}.
     */
    private static Person createPersonWithAddedTags(Person personToEdit, Set<Tag> tagSet) {
        assert personToEdit != null;

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tagSet);

        return new Person(
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
        return index.equals(otherAddTagCommand.index) && tagSet.equals(otherAddTagCommand.tagSet);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tags", tagSet)
                .toString();
    }
}
