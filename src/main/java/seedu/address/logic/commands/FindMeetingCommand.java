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
import seedu.address.model.meeting.MeetingMatchesKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;

/**
 * Finds and lists all meetings in the current displayed meeting list whose specific
 * parameters contains any of the argument keywords. Keyword matching is case-insensitive.
 */
public class FindMeetingCommand extends Command {

    public static final String COMMAND_WORD = "findmeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all meetings whose parameters contain any of "
            + "the specified keywords (case-insensitive) on the current displayed list "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: [d/ SEARCH SUBSTRING] [dt/ SEARCH SUBSTRING] [i/ PERSON INDICES]...\n"
            + "Example: " + COMMAND_WORD + " d/ meeting dt/ 2026";

    public static final String MESSAGE_FORMAT =
            "Format: findmeeting [d/ SEARCH SUBSTRING] [dt/ SEARCH SUBSTRING] [i/ PERSON INDICES]...\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " d/ meeting"
                    + " dt/ 2026"
                    + " i/ 1, 2, 3";

    public static final String MESSAGE_NO_PARAMS_FOUND =
            "No description, date or indices have been detected." + "\n" + MESSAGE_FORMAT;

    private final List<String> descriptionKeywords;
    private final List<String> dateKeywords;
    private final Set<Index> personIndices;

    /**
     * Instantiates a new FindMeetingCommand with the respective parameters.
     *
     * @param descriptionKeywords the keywords corresponding to the meeting description.
     * @param dateKeywords the keywords corresponding to the meeting date.
     * @param personIndices the indices corresponding to the current person list's contact indices.
     */
    public FindMeetingCommand(List<String> descriptionKeywords,
                              List<String> dateKeywords,
                              Set<Index> personIndices) {
        this.descriptionKeywords = descriptionKeywords;
        this.dateKeywords = dateKeywords;
        this.personIndices = new HashSet<>(personIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personList = model.getFilteredPersonList();

        Set<PersonId> personIdsToMatch = new HashSet<>();
        for (Index index : personIndices) {
            if (index.getZeroBased() >= personList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personIdsToMatch.add(personList.get(index.getZeroBased()).getId());
        }

        MeetingMatchesKeywordsPredicate predicate =
                new MeetingMatchesKeywordsPredicate(descriptionKeywords, dateKeywords, personIdsToMatch);

        model.updateFilteredMeetingListStacked(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_MEETINGS_LISTED_OVERVIEW, model.getFilteredMeetingList().size()));
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

        FindMeetingCommand otherFindMeetingCommand = (FindMeetingCommand) other;

        return descriptionKeywords.equals(otherFindMeetingCommand.descriptionKeywords)
                && dateKeywords.equals(otherFindMeetingCommand.dateKeywords)
                && personIndices.equals(otherFindMeetingCommand.personIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("descriptionKeywords", descriptionKeywords)
                .add("dateKeywords", dateKeywords)
                .add("personIndices", personIndices)
                .toString();
    }
}

