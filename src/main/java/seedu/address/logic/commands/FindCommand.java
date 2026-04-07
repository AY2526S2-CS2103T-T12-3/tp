package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Finds and lists all persons in current displayed contact list whose name, email or phone
 * contains any of the argument keywords. Keyword matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches in the displayed contact list for those"
            + " that match any one of the given keywords.\n"
            + "You can search using a global substring across name, phone, and email fields.\n"
            + "Alternatively, you can specify fields to search.\n"
            + "Global Find: Format: " + COMMAND_WORD + " SEARCH_SUBSTRING\n"
            + "Example: " + COMMAND_WORD + " John\n"
            + "Field-Specific Find: Format: " + COMMAND_WORD + " (n/NAME)... (p/PHONE)... (e/EMAIL)...\n"
            + "Example: " + COMMAND_WORD + " n/John p/98765432 e/example@email.com\n"
            + "Note: Search is case-insensitive.\n";

    private final PersonMatchesKeywordsPredicate predicate;

    /**
     * Creates a FindCommand to find the specified {@code Person}s given the {@code predicate}.
     *
     * @param predicate The predicate to look for in the current persons' list.
     */
    public FindCommand(PersonMatchesKeywordsPredicate predicate) {
        requireNonNull(predicate);

        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredPersonListStacked(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
