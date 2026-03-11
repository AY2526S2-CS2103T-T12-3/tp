package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
 * Deletes the specified tags from the persons identified using their displayed indices from the address book.
 */
public class DeleteTagCommand extends Command {
    public static final String COMMAND_WORD = "deletetag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified tag(s) from the person(s) "
            + "identified by the index number(s) used in the displayed person list.\n";

    public static final String MESSAGE_FORMAT =
            "(Format: deletetag INDEX, ... / TAG [/ TAG] ...)\n"
            + "Example: "
            + COMMAND_WORD
            + " 1, 2, 3"
            + " / computer science"
            + " / friend";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Removed tags %1$s from specified persons";

    private final List<Index> targetIndices;
    private final Set<Tag> tags;

    /**
     * Acts as the constructor for DeleteTagCommand.
     *
     * @param targetIndices The target indices representing the persons to be edited.
     * @param tags The tags to be removed from the specified persons.
     */
    public DeleteTagCommand(List<Index> targetIndices, Set<Tag> tags) {
        this.targetIndices = targetIndices;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Boolean hasAtLeastOneValidTag = false;

        for (int j = 0; j < targetIndices.size(); j++) {
            if (targetIndices.get(j).getZeroBased() >= lastShownList.size()) {
                throw new CommandException("Error: " + Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        for (int i = 0; i < targetIndices.size(); i++) {
            // edits the tags in each person and sets the edited person
            Index index = targetIndices.get(i);
            Person person = lastShownList.get(index.getZeroBased());

            Set<Tag> updatedTags = new HashSet<>(person.getTags());
            for (Tag tag : tags) {
                if (updatedTags.contains(tag)) {
                    hasAtLeastOneValidTag = true;
                }
                updatedTags.remove(tag);
            }

            if (!hasAtLeastOneValidTag) {
                throw new CommandException(
                        "Error: None of the specified tags exist in any of the specified contacts.");
            }

            Person editedPerson = new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    updatedTags
            );

            model.setPerson(person, editedPerson);
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, tags.toString()));
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

        Boolean isEqual = true;

        for (int i = 0; i < targetIndices.size(); i++) {
            if (!isEqual) {
                break;
            }
            isEqual = isEqual && targetIndices.get(i).equals(otherDeleteTagCommand.targetIndices.get(i));
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
