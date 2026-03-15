package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonComparatorTest {

    @Test
    public void compare_starredBeforeUnstarred() {
        Person starred = new PersonBuilder().withName("Bob").withTags(Tag.STAR_TAG).build();
        Person unstarred = new PersonBuilder().withName("Alice").build();

        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(starred, unstarred) < 0);
    }

    @Test
    public void compare_unstarredAfterStarred() {
        Person starred = new PersonBuilder().withName("Bob").withTags(Tag.STAR_TAG).build();
        Person unstarred = new PersonBuilder().withName("Alice").build();

        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(unstarred, starred) > 0);
    }

    @Test
    public void compare_twoStarred_alphabetical() {
        Person alice = new PersonBuilder().withName("Alice").withTags(Tag.STAR_TAG).build();
        Person bob = new PersonBuilder().withName("Bob").withTags(Tag.STAR_TAG).build();

        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(alice, bob) < 0);
        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(bob, alice) > 0);
    }

    @Test
    public void compare_twoUnstarred_alphabetical() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();

        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(alice, bob) < 0);
        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(bob, alice) > 0);
    }

    @Test
    public void compare_samePerson_returnsZero() {
        Person alice = new PersonBuilder().withName("Alice").withTags(Tag.STAR_TAG).build();

        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(alice, alice) == 0);
    }

    @Test
    public void compare_caseInsensitive() {
        Person alice = new PersonBuilder().withName("alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();

        assertTrue(PersonComparator.STAR_FIRST_ALPHABETICAL.compare(alice, bob) < 0);
    }

    @Test
    public void sort_mixedList_correctOrder() {
        Person charlie = new PersonBuilder().withName("Charlie").build();
        Person alice = new PersonBuilder().withName("Alice").withTags(Tag.STAR_TAG).build();
        Person bob = new PersonBuilder().withName("Bob").build();
        Person david = new PersonBuilder().withName("David").withTags(Tag.STAR_TAG).build();

        List<Person> persons = new ArrayList<>(List.of(charlie, alice, bob, david));
        persons.sort(PersonComparator.STAR_FIRST_ALPHABETICAL);

        assertTrue(persons.get(0).getName().fullName.equals("Alice"));
        assertTrue(persons.get(1).getName().fullName.equals("David"));
        assertTrue(persons.get(2).getName().fullName.equals("Bob"));
        assertTrue(persons.get(3).getName().fullName.equals("Charlie"));
    }
}
