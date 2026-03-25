package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PersonMatchesKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstGlobal = Collections.singletonList("first");
        List<String> secondGlobal = Arrays.asList("first", "second");

        PersonMatchesKeywordsPredicate firstPredicate =
            new PersonMatchesKeywordsPredicate(firstGlobal, List.of(), List.of(), List.of());

        PersonMatchesKeywordsPredicate secondPredicate =
            new PersonMatchesKeywordsPredicate(secondGlobal, List.of(), List.of(), List.of());

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonMatchesKeywordsPredicate firstPredicateCopy =
                new PersonMatchesKeywordsPredicate(firstGlobal, List.of(), List.of(), List.of());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameMatches_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        List.of("Alice"),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_phoneMatches_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        List.of("9435"),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_emailMatches_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        List.of("EXAmple")
                );

        assertTrue(predicate.test(ALICE));
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

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_globalKeywordsMatchesAnyField_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        List.of("alice"), // global
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(ALICE));
    }


    @Test
    public void test_globalSubstringMatchesAnyField_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(
                        List.of("al"), // global
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()
                );

        assertTrue(predicate.test(ALICE));
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

        assertTrue(predicate.test(ALICE));
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

        assertFalse(predicate.test(ALICE));
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

        assertFalse(predicate.test(ALICE));
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

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_multipleGlobalKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(
                Arrays.asList("alice", "9435", "example"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertTrue(predicate.test(ALICE));
    }
}
