package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEWTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OLDTAG;

import java.util.ArrayList;
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
 * Edits the specified tag (case-insensitive) for the persons identified
 * using their indices from the displayed contact list, or globally.
 */
public class EditTagCommand extends Command {
    public static final String COMMAND_WORD = "edittag";

    public static final String MESSAGE_FORMAT = "Formats:\n"
                    + "1. " + COMMAND_WORD + " INDEX [,INDEX]... "
                    + PREFIX_OLDTAG + "OLD_TAG "
                    + PREFIX_NEWTAG + "NEW_TAG\n"
                    + "Note: INDEX must be a positive integer\n"
                    + "2. " + COMMAND_WORD + " all "
                    + PREFIX_OLDTAG + "OLD_TAG "
                    + PREFIX_NEWTAG + "NEW_TAG\n"
                    + "Note: Use 'all' to apply to all entries\n";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the specified tag (case-insensitive) for the person(s) "
            + "identified by their index number(s) in the displayed contact list.\n"
            + "You can also edit the tag globally in the displayed contact list using \"all\".\n"
            + MESSAGE_FORMAT;

    public static final String MESSAGE_EDIT_TAG_SUCCESS_INDICES =
            "The tag %1$s has been changed to %2$s for the specified persons.";

    public static final String MESSAGE_EDIT_TAG_SUCCESS_GLOBAL =
            "The tag %1$s has been changed to %2$s for all persons.";

    public static final String MESSAGE_OLDTAG_INVALID =
            "Error: The specified old tag (o/) does not exist in any of the specified persons.";

    private Tag oldTag;
    private Tag newTag;
    private final Set<Index> targetIndices;

    /**
     * Acts as the constructor for EditTagCommand.
     *
     * @param targetIndices The target indices representing the persons to be edited.
     * @param oldTag The existing tag to be changed from the specified persons.
     * @param newTag The tag that the old tag is to be changed to. Can be an existing tag that is not the old tag.
     */
    public EditTagCommand(Set<Index> targetIndices, Tag oldTag, Tag newTag) {
        this.targetIndices = new HashSet<>(targetIndices);
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Set<Index> resolvedIndices = getResolvedIndices(lastShownList, targetIndices);

        editSpecifiedPersons(model, lastShownList, resolvedIndices);

        if (resolvedIndices.size() == lastShownList.size()) {
            // global edit
            return new CommandResult(
                    String.format(MESSAGE_EDIT_TAG_SUCCESS_GLOBAL, oldTag.toString(), newTag.toString()));
        }

        return new CommandResult(String.format(
                MESSAGE_EDIT_TAG_SUCCESS_INDICES,
                oldTag.toString(),
                newTag.toString()));
    }

    /**
     * Returns the final set of person indices to be operated on in the EditTagCommand.
     *
     * @param lastShownList The currently displayed contact list.
     * @param targetIndices The set of indices initially passed into the command.
     * @return The final set of person indices to be operated on.
     */
    private static Set<Index> getResolvedIndices(List<Person> lastShownList, Set<Index> targetIndices)
            throws CommandException {
        Set<Index> resolvedIndices = new HashSet<>(); // targetIndices is final, so extra safety to not modify it

        if (targetIndices.isEmpty()) {
            // global edit, add all valid indices to resolvedIndices
            for (int i = 0; i < lastShownList.size(); i++) {
                resolvedIndices.add(Index.fromOneBased(i + 1));
            }
        } else {
            resolvedIndices = new HashSet<>(targetIndices);
        }

        // checks for any invalid indices
        for (Index index : resolvedIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        return resolvedIndices;
    }

    /**
     * Edits the tags for the specified persons based on their given indices.
     *
     * @param model The model used in the command.
     * @param lastShownList The currently displayed contact list.
     * @param resolvedIndices The set of person indices to be operated on.
     * @throws CommandException if none of the given contacts contain the specified old tag.
     */
    private void editSpecifiedPersons(Model model, List<Person> lastShownList, Set<Index> resolvedIndices)
            throws CommandException {
        boolean isOldTagValid = false;
        List<Person> personsToEdit = new ArrayList<>();

        for (Index index : resolvedIndices) {
            personsToEdit.add(lastShownList.get(index.getZeroBased()));
        }

        for (Person person : personsToEdit) {
            // checks if the old tag given is valid for at least one of the specified contacts
            if (person.getTags().contains(oldTag)) {
                isOldTagValid = true;
                break;
            }
        }

        if (!isOldTagValid) {
            throw new CommandException(MESSAGE_OLDTAG_INVALID);
        }

        for (Person person : personsToEdit) {
            // edits the tag in each person and sets the edited person
            Person editedPerson = createPersonWithEditedTags(person, oldTag, newTag);
            model.setPerson(person, editedPerson);
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * with the new tag.
     */
    private static Person createPersonWithEditedTags(Person personToEdit, Tag oldTag, Tag newTag) {
        assert personToEdit != null;

        Set<Tag> updatedTags = new HashSet<>();

        for (Tag tag : personToEdit.getTags()) {
            if (tag.equals(oldTag)) {
                updatedTags.add(new Tag(newTag.tagName));
            } else {
                updatedTags.add(tag);
            }
        }

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
        if (!(other instanceof EditTagCommand)) {
            return false;
        }

        EditTagCommand otherEditTagCommand = (EditTagCommand) other;

        return targetIndices.equals(otherEditTagCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .add("oldTag", oldTag)
                .add("newTag", newTag)
                .toString();
    }
}
