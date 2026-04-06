package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAnyNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: necessary details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final PersonId id;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();


    /**
     * Constructs a {@code Person} with a given ID.
     */
    public Person(PersonId id, Name name, Phone phone, Email email, Set<Tag> tags) {
        assert id != null : "id should not be null";
        assert name != null : "name should not be null";
        assert tags != null : "tags should not be null";
        assert phone != null || email != null : "phone and email cannot both be null";

        requireAllNonNull(id, name, tags);
        requireAnyNonNull(phone, email);

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
    }

    /**
     * Constructs a {@code Person} without a given ID.
     * Generates a new random PersonId automatically.
     */
    public Person(Name name, Phone phone, Email email, Set<Tag> tags) {
        this(new PersonId(), name, phone, email, tags);
    }

    // Returns a defensive copy of the parameters.
    public PersonId getId() {
        assert id != null : "id should not be null when accessed";
        return new PersonId(id.toString());
    }

    public Name getName() {
        assert name != null : "name should not be null when accessed";
        return new Name(name.toString());
    }

    public Phone getPhone() {
        assert phone != null || email != null : "Person must have phone or email";
        return phone == null ? null : new Phone(phone.toString());
    }

    public Email getEmail() {
        assert phone != null || email != null : "Person must have phone or email";
        return email == null ? null : new Email(email.toString());
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        assert tags != null : "tags should not be null when accessed";
        return Collections.unmodifiableSet(tags);
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
        assert otherPerson != null : "otherPerson should not be null";

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
        assert otherPerson.id != null : "otherPerson id should not be null";
        assert otherPerson.name != null : "otherPerson name should not be null";
        assert otherPerson.tags != null : "otherPerson tags should not be null";

        return Objects.equals(id, otherPerson.id)
                && Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(tags, otherPerson.tags);
    }

    @Override
    public int hashCode() {
        assert id != null : "id should not be null when hashing";
        assert name != null : "name should not be null when hashing";
        assert tags != null : "tags should not be null when hashing";
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, tags);
    }

    @Override
    public String toString() {
        assert id != null : "id should not be null when converting to string";
        assert name != null : "name should not be null when converting to string";
        assert tags != null : "tags should not be null when converting to string";
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .toString();
    }
}
