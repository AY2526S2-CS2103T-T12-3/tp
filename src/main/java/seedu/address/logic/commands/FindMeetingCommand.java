package seedu.address.logic.commands;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

import static java.util.Objects.requireNonNull;

/**
 * Finds and lists all meetings in address book whose specific parameters contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindMeetingCommand extends Command {

    public static final String COMMAND_WORD = "findmeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all meetings whose parameters contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: [d/ SEARCH SUBSTRING] [dt/ SEARCH SUBSTRING] [i/ PERSON INDICES]...\n"
            + "Example: " + COMMAND_WORD + " d/ meeting dt/ 2026";

    private final PersonMatchesKeywordsPredicate predicate;

    public FindMeetingCommand(PersonMatchesKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindMeetingCommand)) {
            return false;
        }

        FindMeetingCommand otherFindCommand = (FindMeetingCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
