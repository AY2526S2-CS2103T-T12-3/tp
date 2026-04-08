package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Unstars the person(s) identified using their displayed indices from the current displayed contact list.
 * This is basically the same as removing a tag with the reserved 'STAR' keyword.
 */
public class UnstarCommand extends Command {

    public static final String COMMAND_WORD = "unstar";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unstars / Unfavourites the person(s) identified by their indices in the displayed contact list.\n"
            + "Format: " + COMMAND_WORD + " INDEX [, INDEX]...\n"
            + "Note: INDEX must be a positive integer\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNSTAR_PERSON_SUCCESS = "Unstarred Person(s): %1$s";

    public static final String MESSAGE_NO_VALID_PERSONS_UNSTAR =
            "Error: All people provided are already unstarred.";

    private final Set<Index> targetIndices;

    public UnstarCommand(Set<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Index> sortedIndices = new ArrayList<>(targetIndices);
        sortedIndices.sort(Comparator.comparingInt(Index::getZeroBased));

        List<Person> personsToUnstar = getPersonsToUnstar(model, sortedIndices);
        List<Person> unstarredPersons = setPersonsToUnstar(model, personsToUnstar);

        String unstarredPersonsString = unstarredPersons.stream()
                .map(person -> person.getName().fullName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_UNSTAR_PERSON_SUCCESS, unstarredPersonsString));

    }

    public List<Person> setPersonsToUnstar(Model model, List<Person> personsToUnstar) throws CommandException {
        for (Person personToUnstar : personsToUnstar) {
            Set<Tag> newTags = new HashSet<>(personToUnstar.getTags());
            newTags.remove(new Tag(Tag.STAR_TAG));

            Person unstarredPerson = new Person(
                    personToUnstar.getId(),
                    personToUnstar.getName(),
                    personToUnstar.getPhone(),
                    personToUnstar.getEmail(),
                    newTags
            );

            model.setPerson(personToUnstar, unstarredPerson);
        }

        return personsToUnstar;
    }

    private static List<Person> getPersonsToUnstar(Model model, List<Index> targetIndices) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> personsToUnstar = new ArrayList<>();

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToUnstar = lastShownList.get(index.getZeroBased());

            if (!personToUnstar.getTags().contains(new Tag(Tag.STAR_TAG))) {
                continue; // already unstarred, skip
            }

            personsToUnstar.add(personToUnstar);
        }

        if (personsToUnstar.isEmpty()) {
            throw new CommandException(MESSAGE_NO_VALID_PERSONS_UNSTAR);
        }

        return personsToUnstar;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnstarCommand)) {
            return false;
        }

        UnstarCommand otherCommand = (UnstarCommand) other;
        return targetIndices.equals(otherCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
