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
    private final Set<PersonId> idsToMatch;

    /**
     * Constructor for MeetingMatchesKeywordsPredicate class
     *
     * @param descriptionKeywords substring to match for Meeting descriptions
     * @param dateKeywords substring to match for Meeting dates
     * @param idsToMatch set to match for Meeting participant {@code PersonId}s
     */
    public MeetingMatchesKeywordsPredicate(List<String> descriptionKeywords,
                                           List<String> dateKeywords,
                                           Set<PersonId> idsToMatch) {
        this.descriptionKeywords = descriptionKeywords;
        this.dateKeywords = dateKeywords;
        this.idsToMatch = idsToMatch;
    }

    @Override
    public boolean test(Meeting meeting) {
        String description = meeting.getDescription().description.toLowerCase();
        String date = meeting.getDate().toString();
        Set<PersonId> participantIDs = meeting.getParticipantsIDs();

        boolean doAnyKeywordsMatchDescription = descriptionKeywords
                .stream()
                .anyMatch(substring -> description.contains(substring.toLowerCase()));

        boolean doAnyKeywordsMatchDate = dateKeywords
                .stream()
                .anyMatch(substring -> date.contains(substring.toLowerCase()));

        boolean doesMeetingContainAllUuids = !idsToMatch.isEmpty()
                && participantIDs.containsAll(idsToMatch);

        return doAnyKeywordsMatchDescription || doAnyKeywordsMatchDate || doesMeetingContainAllUuids;
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
                && idsToMatch.equals(otherPredicate.idsToMatch);
    }

}
