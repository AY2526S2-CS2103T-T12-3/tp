package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
 * Edits the specified tag for the persons identified using their displayed indices from the address book, or globally.
 */
public class EditTagCommand extends Command {
    public static final String COMMAND_WORD = "edittag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the specified tag for the person(s) "
            + "identified by the index number(s) used in the displayed person list, or globally.\n";

    public static final String MESSAGE_FORMAT =
            "(Format: edittag [INDICES or 'all'] o/ OLDTAG n/ NEWTAG)\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " 1, 2, 3"
                    + " o/ acquaintance"
                    + " n/ friend";

    public static final String MESSAGE_EDIT_TAG_SUCCESS_INDICES =
            "The tag %1$s has been changed to %2$s for the specified contacts.";

    public static final String MESSAGE_EDIT_TAG_SUCCESS_GLOBAL =
            "The tag %1$s has been changed to %2$s for all contacts.";

    public static final String MESSAGE_OLDTAG_INVALID =
            "Error: The specified old tag (o/) does not exist in any of the specified contacts.";

    private Tag oldTag;
    private Tag newTag;
    private final List<Index> targetIndices;

    /**
     * Acts as the constructor for EditTagCommand.
     *
     * @param targetIndices The target indices representing the persons to be edited.
     * @param oldTag The existing tag to be changed from the specified persons.
     * @param newTag The tag that the old tag is to be changed to. Can be an existing tag that is not the old tag.
     */
    public EditTagCommand(List<Index> targetIndices, Tag oldTag, Tag newTag) {
        this.targetIndices = new ArrayList<>(targetIndices);
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        boolean isOldTagValid = false;

        List<Index> resolvedIndices = new ArrayList<>(); // targetIndices is final, so extra safety to not modify it

        if (targetIndices.isEmpty()) {
            // global edit, add all valid indices to resolvedIndices
            for (int i = 0; i < lastShownList.size(); i++) {
                resolvedIndices.add(Index.fromOneBased(i + 1));
            }
        } else {
            resolvedIndices = new ArrayList<>(targetIndices);
        }

        // First checks if all indices are valid.
        for (int j = 0; j < resolvedIndices.size(); j++) {
            if (resolvedIndices.get(j).getZeroBased() >= lastShownList.size()) {
                throw new CommandException("Error: " + Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Snapshot all target persons BEFORE any edits
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
            throw new CommandException(
                    "Error: The specified old tag (o/) does not exist in any of the specified contacts.");
        }

        for (Person person : personsToEdit) {
            // edits the tag in each person and sets the edited person
            Person editedPerson = createPersonWithEditedTags(person, oldTag, newTag);
            model.setPerson(person, editedPerson);
        }

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
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * with the new tag.
     */
    private static Person createPersonWithEditedTags(Person personToEdit, Tag oldTag, Tag newTag) {
        assert personToEdit != null;

        Set<Tag> updatedTags = new HashSet<>();

        for (Tag tag : personToEdit.getTags()) {
            if (tag.tagName.equals(oldTag.tagName)) {
                updatedTags.add(new Tag(newTag.tagName));
            } else {
                updatedTags.add(tag);
            }
        }

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                updatedTags,
                personToEdit.getMeetings()
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

        Boolean isEqual = true;

        for (int i = 0; i < targetIndices.size(); i++) {
            if (!isEqual) {
                break;
            }
            isEqual = isEqual && targetIndices.get(i).equals(otherEditTagCommand.targetIndices.get(i));
        }
        return isEqual;
    }

    @Override
    public String toString() {
        ToStringBuilder stringBuilder = new ToStringBuilder(this);

        for (int i = 0; i < targetIndices.size(); i++) {
            stringBuilder.add("targetIndex", targetIndices.get(i));
        }
        return stringBuilder.toString();
    }
}
