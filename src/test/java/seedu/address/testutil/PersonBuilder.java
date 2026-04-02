package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ID = "00000000-0000-0000-0000-000000000001";

    private PersonId id;
    private Name name;
    private Phone phone;
    private Email email;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        id = new PersonId(DEFAULT_ID);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        tags = new HashSet<>(personToCopy.getTags());
        id = personToCopy.getId() != null ? personToCopy.getId() : null;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     * If {@code phone} is null, the Person will have no phone.
     */
    public PersonBuilder withPhone(String phone) {
        if (phone == null) {
            this.phone = null; // skip creating Phone object
        } else {
            this.phone = new Phone(phone);
        }
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     * If {@code email} is null, the Person will have no email.
     */
    public PersonBuilder withEmail(String email) {
        if (email == null) {
            this.email = null; // skip creating Email object
        } else {
            this.email = new Email(email);
        }
        return this;
    }

    /**
     * Sets the {@code PersonId} of the {@code Person} that we are building.
     * If {@code id} is null or empty, the Person will generate a new PersonId.
     */
    public PersonBuilder withId(String id) {
        this.id = new PersonId(id);
        return this;
    }

    /**
     * Builds the {@code Person} object with all the set fields.
     * If {@code id} is null or empty, the constructor that generates a new ID will be used.
     */
    public Person build() {
        if (id == null) {
            // no ID set, generate automatically
            return new Person(name, phone, email, tags);
        } else {
            // use the specified PersonId
            return new Person(id, name, phone, email, tags);
        }
    }
}
