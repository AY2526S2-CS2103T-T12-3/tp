package seedu.address.model.person;

import java.util.Comparator;

import seedu.address.model.tag.Tag;

/**
 * Comparator for Persons in UniquePersonList
 * Starred Contacts will show at the front of the list
 * Contacts will be sorted in alphabetical order
 */
public class PersonComparator {
    public static final Comparator<Person> STAR_FIRST_ALPHABETICAL =
            Comparator
                .comparing((Person p) -> !p.getTags().contains(new Tag(Tag.STAR_TAG)))
                .thenComparing(p -> p.getName().fullName.toLowerCase());
}
