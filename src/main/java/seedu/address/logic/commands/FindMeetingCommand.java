package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDICES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;

import java.util.ArrayList;
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
 * Finds and lists all meetings in the displayed meeting list whose specific
 * fields match any of the given inputs.
 */
public class FindMeetingCommand extends Command {

    public static final String COMMAND_WORD = "findmeet";

    public static final String MESSAGE_FORMAT =
            "Format: " + COMMAND_WORD + " "
                    + "(" + PREFIX_MEETING_DESCRIPTION + "DESCRIPTION)... "
                    + "(" + PREFIX_MEETING_DATE + "DATE)... "
                    + "(" + PREFIX_CONTACT_INDICES + "CONTACT_INDEX "
                    + "[,CONTACT_INDEX]...)...\n"
                    + "Note: CONTACT_INDEX must be a positive integer\n"
                    + "Note: Indices within a single i/ (e.g. i/1,2,3) are treated as AND — "
                    + "the meeting must include all specified contacts\n"
                    + "Multiple i/ prefixes (e.g. i/1 i/2,3) are treated as OR — "
                    + "the meeting matches if it satisfies any one of the i/ prefixes\n"
                    + "Example: " + COMMAND_WORD + " "
                    + PREFIX_MEETING_DESCRIPTION + "meeting "
                    + PREFIX_MEETING_DATE + "2026 "
                    + PREFIX_CONTACT_INDICES + "1 "
                    + PREFIX_CONTACT_INDICES + "1,2,3";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds meetings in the displayed meeting list by matching the given inputs "
            + "against the specified fields. A meeting is included if any specified field matches.\n"
            + "Description and date fields are matched using case-insensitive substrings, "
            + "and indices are matched by if the meetings contains the specified contact indices as participants.\n"
            + MESSAGE_FORMAT;

    public static final String MESSAGE_NO_PARAMS_FOUND =
            "No description, date or indices have been detected.\n" + MESSAGE_USAGE;

    private final List<String> descriptionKeywords;
    private final List<String> dateKeywords;
    private final List<Set<Index>> personIndexGroups;

    /**
     * Instantiates a new FindMeetingCommand with the respective parameters.
     *
     * @param descriptionKeywords The keywords corresponding to the meeting description.
     * @param dateKeywords The keywords corresponding to the meeting date.
     * @param personIndexGroups The groups of indices corresponding to the current contact list's indices.
     *                          Indices within a group are matched using AND, while groups are matched using OR.
     */
    public FindMeetingCommand(List<String> descriptionKeywords,
                              List<String> dateKeywords,
                              List<Set<Index>> personIndexGroups) {
        this.descriptionKeywords = descriptionKeywords;
        this.dateKeywords = dateKeywords;
        this.personIndexGroups = new ArrayList<>();
        for (Set<Index> group : personIndexGroups) {
            this.personIndexGroups.add(new HashSet<>(group));
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personList = model.getFilteredPersonList();

        List<Set<PersonId>> personIdGroupsToMatch = new ArrayList<>();
        for (Set<Index> indexGroup : personIndexGroups) {
            Set<PersonId> personIdsInGroup = new HashSet<>();

            for (Index index : indexGroup) {
                if (index.getZeroBased() >= personList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                personIdsInGroup.add(personList.get(index.getZeroBased()).getId());
            }

            personIdGroupsToMatch.add(personIdsInGroup);
        }

        MeetingMatchesKeywordsPredicate predicate =
                new MeetingMatchesKeywordsPredicate(descriptionKeywords, dateKeywords, personIdGroupsToMatch);

        model.updateFilteredMeetingListStacked(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_MEETINGS_LISTED_OVERVIEW, model.getFilteredMeetingList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindMeetingCommand)) {
            return false;
        }

        FindMeetingCommand otherFindMeetingCommand = (FindMeetingCommand) other;

        return descriptionKeywords.equals(otherFindMeetingCommand.descriptionKeywords)
                && dateKeywords.equals(otherFindMeetingCommand.dateKeywords)
                && personIndexGroups.equals(otherFindMeetingCommand.personIndexGroups);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("descriptionKeywords", descriptionKeywords)
                .add("dateKeywords", dateKeywords)
                .add("personIndexGroups", personIndexGroups)
                .toString();
    }
}

