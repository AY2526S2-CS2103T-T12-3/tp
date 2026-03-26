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
 * Filters the specified tags and displays the filtered contact list.
 */
public class FilterTagCommand extends Command {
    public static final String COMMAND_WORD = "filtertag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters the specified tags and displays the filtered contact list.\n";

    public static final String MESSAGE_FORMAT =
            "(Format: filtertag / TAG [/ TAG]...)\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " / classmates"
                    + " / family"
                    + " / friends";

    public static final String MESSAGE_FILTER_TAG_SUCCESS = "Filtered contact list by tags: %1$s";
    public static final String MESSAGE_NO_TAGS = "At least one tag must be provided." + "\n" + MESSAGE_FORMAT;
    public static final String MESSAGE_NO_VALID_TAG =
            "Error: None of the tags given belong to any contact in the list.";

    private final Set<Tag> tags;

    /**
     * Acts as the constructor for FilterTagCommand.
     *
     * @param tags The tags to filter the contact list by
     */
    public FilterTagCommand(Set<Tag> tags) {
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

        boolean doesAnyTagMatch = model.getAddressBook().getPersonList()
                .stream()
                .anyMatch(hasAnyTag);

        if (!doesAnyTagMatch) {
            throw new CommandException(MESSAGE_NO_VALID_TAG);
        }

        model.updateFilteredPersonList(hasAnyTag);

        return new CommandResult(String.format(MESSAGE_FILTER_TAG_SUCCESS, tagList));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterTagCommand)) {
            return false;
        }

        FilterTagCommand otherFilterTagCommand = (FilterTagCommand) other;

        return tags.equals(otherFilterTagCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tags", tags)
                .toString();
    }
}
