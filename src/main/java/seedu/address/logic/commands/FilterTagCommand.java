package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    public static final String MESSAGE_NO_TAGS = "At least one tag must be provided.";

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
        List<Person> lastShownList = model.getFilteredPersonList();

        String tagList = tags.stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(", "));

        Boolean hasAtLeastOneValidTag = false;

        for (Person person : lastShownList) {
            // checks whether at least one of given tags exist for at least one contact in the list
            if (!Collections.disjoint(person.getTags(), tags)) {
                hasAtLeastOneValidTag = true;
            }
        }

        if (!hasAtLeastOneValidTag) {
            throw new CommandException(
                    "Error: None of the specified tags exist in any of the contacts in the current list.");
        }


        Predicate<Person> hasAnyTag = person -> {
            for (Tag tag : tags) {
                if (person.getTags().contains(tag)) {
                    return true;
                }
            }
            return false;
        };

        model.updateFilteredPersonList(hasAnyTag);

        boolean doesAnyTagMatch = model.getFilteredPersonList()
                .stream()
                .anyMatch(hasAnyTag);

        if (!doesAnyTagMatch) {
            throw new CommandException("Error: None of the tags given belong to any contact in the list.");
        }

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

        Boolean isEqual = true;

        for (int i = 0; i < tags.size(); i++) {
            if (!isEqual) {
                break;
            }
            isEqual = isEqual && tags.equals(otherFilterTagCommand.tags);
        }
        return isEqual;
    }

    @Override
    public String toString() {
        ToStringBuilder stringBuilder = new ToStringBuilder(this);

        stringBuilder.add("tags", tags);

        return stringBuilder.toString();
    }
}