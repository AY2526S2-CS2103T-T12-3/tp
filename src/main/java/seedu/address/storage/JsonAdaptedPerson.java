package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String id;
    private final String name;
    private final String phone;
    private final String email;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email,
            @JsonProperty("tags") List<JsonAdaptedTag> tags
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;

        if (tags != null) {
            this.tags.addAll(tags);
        }

        assert this.tags != null : "tags should be initialized";
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        id = source.getId().toString();
        name = source.getName().fullName;
        phone = source.getPhone() != null ? source.getPhone().value : null;
        email = source.getEmail() != null ? source.getEmail().value : null;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final Name modelName = toModelName();

        // Validate phone/email
        if (phone == null && email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName() + " or " + Email.class.getSimpleName()));
        }

        Phone modelPhone = toModelPhone();
        Email modelEmail = toModelEmail();

        final Set<Tag> modelTags = toModelTags();

        // If IllegalArgumentException returned, call constructor that generates new ID
        final PersonId modelId;
        try {
            modelId = new PersonId(id.trim());
        } catch (IllegalArgumentException e) {
            return new Person(modelName, modelPhone, modelEmail, modelTags);
        }
        return new Person(modelId, modelName, modelPhone, modelEmail, modelTags);
    }

    /**
     * Converts the stored name string into a {@code Name} object.
     *
     * @return The model {@code Name}.
     * @throws IllegalValueException If the {@code name} is {@code null} or invalid.
     */
    private Name toModelName() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name.trim());
    }

    /**
     * Converts the stored phone string into a {@code Phone} object, if present.
     *
     * @return The model {@code Phone}, or {@code null} if phone is {@code null}.
     * @throws IllegalValueException If phone is invalid.
     */
    private Phone toModelPhone() throws IllegalValueException {
        if (phone != null) {
            if (!Phone.isValidPhone(phone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            return new Phone(phone.trim());
        }
        return null;
    }

    /**
     * Converts the stored email string into an {@code Email} object, if present.
     *
     * @return The model {@code Email}, or {@code null} if email is {@code null}.
     * @throws IllegalValueException If email is invalid.
     */
    private Email toModelEmail() throws IllegalValueException {
        if (email != null) {
            if (!Email.isValidEmail(email)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            return new Email(email.trim());
        }
        return null;
    }

    /**
     * Converts the stored tag representations into a {@code Set<Tag>}.
     *
     * @return A set of model {@code Tag} objects.
     * @throws IllegalValueException If any tag is invalid.
     */
    private Set<Tag> toModelTags() throws IllegalValueException {
        final HashSet<Tag> personTags = new HashSet<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        return personTags;
    }
}
