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
 * Stars a person identified using it's displayed index from the address book.
 * This is basically the same as adding a tag with the reserved 'STAR' keyword
 */
public class StarCommand extends Command {

    public static final String COMMAND_WORD = "star";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Stars / Favourites the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_STAR_PERSON_SUCCESS = "Starred Person: %1$s";

    private final Index targetIndex;

    public StarCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToStar = lastShownList.get(targetIndex.getZeroBased());

        Set<Tag> newTags = new HashSet<>(personToStar.getTags());
        newTags.add(new Tag(Tag.STAR_TAG));

        Person starredPerson = new Person(
            personToStar.getName(),
            personToStar.getPhone(),
            personToStar.getEmail(),
            newTags,
            personToStar.getMeetings()
        );

        model.setPerson(personToStar, starredPerson);

        return new CommandResult(String.format(MESSAGE_STAR_PERSON_SUCCESS, Messages.format(starredPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StarCommand)) {
            return false;
        }

        StarCommand otherStarCommand = (StarCommand) other;
        return targetIndex.equals(otherStarCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
