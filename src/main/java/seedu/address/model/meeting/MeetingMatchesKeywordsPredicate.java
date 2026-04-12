package seedu.address.model.meeting;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.person.PersonId;

/**
 * Tests that a {@code Meeting}'s fields match any of the keywords given.
 */
public class MeetingMatchesKeywordsPredicate implements Predicate<Meeting> {

    private final List<String> descriptionKeywords;
    private final List<String> dateKeywords;
    private final List<Set<PersonId>> idGroupsToMatch;

    /**
     * Constructor for MeetingMatchesKeywordsPredicate class.
     *
     * @param descriptionKeywords Substrings to match for meeting descriptions.
     * @param dateKeywords Substrings to match for meeting dates.
     * @param idGroupsToMatch List of {@code PersonId} groups to match for meeting participants.
     *                        A meeting matches if it contains all ids in any one group.
     */
    public MeetingMatchesKeywordsPredicate(List<String> descriptionKeywords,
                                           List<String> dateKeywords,
                                           List<Set<PersonId>> idGroupsToMatch) {
        this.descriptionKeywords = descriptionKeywords;
        this.dateKeywords = dateKeywords;
        this.idGroupsToMatch = idGroupsToMatch;
    }

    @Override
    public boolean test(Meeting meeting) {
        String description = meeting.getDescription().description.toLowerCase();
        String date = meeting.getDate().toString().toLowerCase();
        Set<PersonId> participantIds = meeting.getParticipantsIDs();

        boolean doAnyKeywordsMatchDescription = descriptionKeywords.stream()
                .anyMatch(substring -> description.contains(substring.toLowerCase()));

        boolean doAnyKeywordsMatchDate = dateKeywords.stream()
                .anyMatch(substring -> date.contains(substring.toLowerCase()));

        boolean doesMeetingMatchAnyIdGroup = idGroupsToMatch.stream()
                .anyMatch(participantIds::containsAll);

        return doAnyKeywordsMatchDescription
                || doAnyKeywordsMatchDate
                || doesMeetingMatchAnyIdGroup;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MeetingMatchesKeywordsPredicate)) {
            return false;
        }

        MeetingMatchesKeywordsPredicate otherPredicate = (MeetingMatchesKeywordsPredicate) other;
        return descriptionKeywords.equals(otherPredicate.descriptionKeywords)
                && dateKeywords.equals(otherPredicate.dateKeywords)
                && idGroupsToMatch.equals(otherPredicate.idGroupsToMatch);
    }
}
