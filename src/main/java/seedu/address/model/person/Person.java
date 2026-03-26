package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAnyNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity field
    private final UUID id;

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Meeting> meetings = new HashSet<>();

    /**
     * Constructs a Person object with the given id
     * Name and either phone or email must be present and not null.
     */
    public Person(UUID id, Name name, Phone phone, Email email, Set<Tag> tags, Set<Meeting> meetings) {
        requireAllNonNull(id, name, tags);
        requireAnyNonNull(phone, email);

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.meetings.addAll(meetings);
    }

    /**
     * Constructs a Person object without any given id
     * Name and either phone or email must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Set<Tag> tags, Set<Meeting> meetings) {
        this(UUID.randomUUID(), name, phone, email, tags, meetings);
    }

    public UUID getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable meetings set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Meeting> getMeetings() {
        return Collections.unmodifiableSet(meetings);
    }

    /**
     * Returns true if both persons are the same.
     * Two persons are considered the same if they have the same name, phone, and email
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }
        return hasSameDetails(otherPerson);
    }

    /**
     * Returns true if both persons have same name, phone and email.
     */
    public boolean hasSameDetails(Person otherPerson) {
        boolean isPhoneBothNull = getPhone() == null && otherPerson.getPhone() == null;
        boolean isPhoneBothNonNullAndEqual = getPhone() != null && getPhone().equals(otherPerson.getPhone());
        boolean isPhoneEqual = isPhoneBothNull || isPhoneBothNonNullAndEqual;

        boolean isEmailBothNull = getEmail() == null && otherPerson.getEmail() == null;
        boolean isEmailBothNonNullAndEqual = getEmail() != null && getEmail().equals(otherPerson.getEmail());
        boolean isEmailEqual = isEmailBothNull || isEmailBothNonNullAndEqual;

        return otherPerson.getName().equals(getName())
                && isPhoneEqual
                && isEmailEqual;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(tags, otherPerson.tags)
                && Objects.equals(meetings, otherPerson.meetings);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, tags, meetings);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("meetings", meetings)
                .toString();
    }

}
