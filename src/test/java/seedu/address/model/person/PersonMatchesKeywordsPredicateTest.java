package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesKeywordsPredicateTest {

    @Test
    public void test_nameMatches_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        List.of("Alice"),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_phoneMatches_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        List.of("9123"),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_emailMatches_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        List.of("gmail")
                );

        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@gmail.com").build()));
    }

    @Test
    public void test_substringMatching_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        List.of("lic"),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_globalKeywords_matchesAnyField_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        List.of("alice"), // global
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("91234567")
                .withEmail("alice@gmail.com")
                .build()));
    }

    @Test
    public void test_multipleFieldsOrLogic_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        List.of("Alice"),
                        List.of("9999"),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("91234567")
                .build()));
    }

    @Test
    public void test_noMatch_returnsFalse() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        List.of("Charlie"),
                        List.of("9999"),
                        List.of("yahoo")
                );

        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("91234567")
                .withEmail("alice@gmail.com")
                .build()));
    }

    @Test
    public void test_emptyKeywords_returnsFalse() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_caseInsensitiveMatching_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        List.of("aLiCe"),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_multipleGlobalKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(
                Arrays.asList("alice", "xyz"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }
}
