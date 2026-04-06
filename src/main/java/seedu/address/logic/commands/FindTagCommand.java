package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Finds for the specified tags in the current displayed contact list
 * and displays the resulting contact list.
 */
public class FindTagCommand extends Command {
    public static final String COMMAND_WORD = "findtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds contacts with the any of the specified tags on the current displayed list "
            + "and displays the filtered contact list.\n";

    public static final String MESSAGE_FORMAT =
            "(Format: findtag / TAG [/ TAG]...)\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " / classmates"
                    + " / family"
                    + " / friends";

    public static final String MESSAGE_FIND_TAG_SUCCESS =
            "Found all contacts in the current list with at least one of these tags: %1$s"
            + "\n"
            + "%2$s persons listed!";
    public static final String MESSAGE_NO_TAGS = "At least one tag must be provided." + "\n" + MESSAGE_FORMAT;
    public static final String MESSAGE_NO_VALID_TAG =
            "Error: None of the tags given belong to any contact in the list.";

    private final Set<Tag> tags;

    /**
     * Acts as the constructor for FindTagCommand.
     *
     * @param tags The tags to filter the contact list by
     */
    public FindTagCommand(Set<Tag> tags) {
        this.tags = new HashSet<>(tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (tags.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS);
        }

        requireNonNull(model);

        String tagList = tags.stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(", "));

        Predicate<Person> hasAnyTag = person -> {
            for (Tag tag : tags) {
                if (person.getTags().contains(tag)) {
                    return true;
                }
            }
            return false;
        };

        boolean doesAnyTagMatch = model.getFilteredPersonList()
                .stream()
                .anyMatch(hasAnyTag);

        if (!doesAnyTagMatch) {
            throw new CommandException(MESSAGE_NO_VALID_TAG);
        }

        model.updateFilteredPersonListStacked(hasAnyTag);

        return new CommandResult(String.format(
                MESSAGE_FIND_TAG_SUCCESS, tagList, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindTagCommand)) {
            return false;
        }

        FindTagCommand otherFindTagCommand = (FindTagCommand) other;

        return tags.equals(otherFindTagCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tags", tags)
                .toString();
    }
}
