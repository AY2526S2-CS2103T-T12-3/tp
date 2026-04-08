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
 * Stars the person(s) identified using their displayed indices from the current displayed list.
 * This is basically the same as adding a tag with the reserved 'STAR' keyword.
 */
public class StarCommand extends Command {

    public static final String COMMAND_WORD = "star";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Stars / Favourites the person(s) identified by their indices in the displayed contact list.\n"
            + "Format: " + COMMAND_WORD + " INDEX [, INDEX]...\n"
            + "Note: INDEX must be a positive integer\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_STAR_PERSON_SUCCESS = "Starred Person(s): %1$s";

    public static final String MESSAGE_NO_VALID_PERSONS_STAR =
            "Error: All people provided are already starred.";

    private final Set<Index> targetIndices;

    public StarCommand(Set<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Index> sortedIndices = new ArrayList<>(targetIndices);
        sortedIndices.sort(Comparator.comparingInt(Index::getZeroBased));

        List<Person> personsToStar = getPersonsToStar(model, sortedIndices);
        List<Person> starredPersons = setPersonsToStar(model, personsToStar);

        String starredPersonsString = starredPersons.stream()
                .map(person -> person.getName().fullName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_STAR_PERSON_SUCCESS, starredPersonsString));
    }

    public List<Person> setPersonsToStar(Model model, List<Person> personsToStar) throws CommandException {
        for (Person personToStar : personsToStar) {
            Set<Tag> newTags = new HashSet<>(personToStar.getTags());
            newTags.add(new Tag(Tag.STAR_TAG));

            Person starredPerson = new Person(
                    personToStar.getId(),
                    personToStar.getName(),
                    personToStar.getPhone(),
                    personToStar.getEmail(),
                    newTags
            );

            model.setPerson(personToStar, starredPerson);
        }

        return personsToStar;
    }

    private static List<Person> getPersonsToStar(Model model, List<Index> targetIndices) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> personsToStar = new ArrayList<>();

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToStar = lastShownList.get(index.getZeroBased());

            if (personToStar.getTags().contains(new Tag(Tag.STAR_TAG))) {
                continue; // already starred, skip
            }

            personsToStar.add(personToStar);
        }

        if (personsToStar.isEmpty()) {
            throw new CommandException(MESSAGE_NO_VALID_PERSONS_STAR);
        }
        return personsToStar;
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
        return targetIndices.equals(otherStarCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
