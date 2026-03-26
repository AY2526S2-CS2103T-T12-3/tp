package seedu.address.storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String INVALID_UUID_MESSAGE = "Invalid UUID format for Person's ID.";

    private final String id;
    private final String name;
    private final String phone;
    private final String email;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedMeeting> meetings = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("meetings") List<JsonAdaptedMeeting> meetings
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (meetings != null) {
            this.meetings.addAll(meetings);
        }
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
        meetings.addAll(source.getMeetings().stream()
                .map(JsonAdaptedMeeting::new)
                .toList());

        meetings.sort(Comparator.comparing(JsonAdaptedMeeting::getDate));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        // Convert tags
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        // Convert meetings
        final List<Meeting> personMeetings = new ArrayList<>();
        for (JsonAdaptedMeeting meeting : meetings) {
            personMeetings.add(meeting.toModelType());
        }

        // Validate name
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        // Validate phone/email
        if (phone == null && email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName() + " or " + Email.class.getSimpleName()));
        }

        Phone modelPhone = null;
        Email modelEmail = null;

        if (phone != null) {
            if (!Phone.isValidPhone(phone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            modelPhone = new Phone(phone);
        }

        if (email != null) {
            if (!Email.isValidEmail(email)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            modelEmail = new Email(email);
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<Meeting> modelMeetings = new HashSet<>(personMeetings);

        // Handle ID: if missing, call constructor that generates new ID
        if (id == null || id.isEmpty()) {
            return new Person(modelName, modelPhone, modelEmail, modelTags, modelMeetings);
        } else {
            final UUID modelId;
            try {
                modelId = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(INVALID_UUID_MESSAGE);
            }
            return new Person(modelId, modelName, modelPhone, modelEmail, modelTags, modelMeetings);
        }
    }
}
