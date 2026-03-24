package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s fields match any of the keywords given.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {

    private final List<String> globalKeywords;
    private final List<String> nameKeywords;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;

    public PersonMatchesKeywordsPredicate(List<String> globalKeywords,
            List<String> nameKeywords,
            List<String> phoneKeywords,
            List<String> emailKeywords) {
        this.globalKeywords = globalKeywords;
        this.nameKeywords = nameKeywords;
        this.phoneKeywords = phoneKeywords;
        this.emailKeywords = emailKeywords;
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName.toLowerCase();
        String phone = person.getPhone().value.toLowerCase();
        String email = person.getEmail().value.toLowerCase();

        // Global search
        if (!globalKeywords.isEmpty()) {
            return globalKeywords.stream().anyMatch(k -> {
                String keyword = k.toLowerCase();
                return name.contains(keyword)
                        || phone.contains(keyword)
                        || email.contains(keyword);
            });
        }

        // Field-specific search
        boolean matchName = nameKeywords.stream()
                .anyMatch(k -> name.contains(k.toLowerCase()));

        boolean matchPhone = phoneKeywords.stream()
                .anyMatch(k -> phone.contains(k.toLowerCase()));

        boolean matchEmail = emailKeywords.stream()
                .anyMatch(k -> email.contains(k.toLowerCase()));

        return matchName || matchPhone || matchEmail;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonMatchesKeywordsPredicate)) {
            return false;
        }

        PersonMatchesKeywordsPredicate otherPredicate = (PersonMatchesKeywordsPredicate) other;
        return globalKeywords.equals(otherPredicate.globalKeywords)
                && nameKeywords.equals(otherPredicate.nameKeywords)
                && phoneKeywords.equals(otherPredicate.phoneKeywords)
                && emailKeywords.equals(otherPredicate.emailKeywords);
    }

}
