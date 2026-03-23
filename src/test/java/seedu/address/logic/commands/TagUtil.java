package seedu.address.logic.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Provides methods to assist with adding tags
 */
public class TagUtil {
    /**
     * Returns a new person with the tags changed to be {@code tags}
     * @param personToEdit The old person to be referenced
     * @param tags The tags to replace
     * @return A new person similar to {@code personToEdit} with only their tags replaced
     */
    public static Person amendTagsOfPerson(Person personToEdit, Collection<Tag> tags) {
        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        tags.forEach(tag -> updatedTags.add(tag));

        return new Person(
                personToEdit.getId(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                updatedTags,
                personToEdit.getMeetings()
        );
    }
}
