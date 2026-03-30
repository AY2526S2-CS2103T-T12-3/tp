package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.logging.Logger;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {
    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

    public static final String MESSAGE_DUPLICATE_PERSON =
            "%s already exists in the address book, skipping person.";
    public static final String MESSAGE_DUPLICATE_MEETING =
            "%s already exists in the meeting list, skipping meeting.";
    public static final String MESSAGE_DUPLICATE_ID =
            "%s has an ID that already exists in the address book, skipping person.";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedMeeting> meetings = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(
            @JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("meetings") List<JsonAdaptedMeeting> meetings) {
        this.persons.addAll(persons);
        this.meetings.addAll(meetings);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).toList());
        meetings.addAll(source.getMeetingList().stream().map(JsonAdaptedMeeting::new).toList());
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     * Logs for all duplicate objects created and skips past these objects.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        // Adds persons
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();

            if (addressBook.hasPerson(person)) {
                logger.warning(String.format(MESSAGE_DUPLICATE_PERSON, person.toString()));
                continue;
            }

            if (addressBook.hasSameID(person)) {
                logger.warning(String.format(MESSAGE_DUPLICATE_ID, person.getName()));
                continue;
            }

            addressBook.addPerson(person);
        }

        // Adds meetings
        for (JsonAdaptedMeeting jsonAdaptedMeeting : meetings) {
            Meeting meeting = jsonAdaptedMeeting.toModelType();

            if (addressBook.hasMeeting(meeting)) {
                logger.warning(String.format(MESSAGE_DUPLICATE_MEETING, meeting.toString()));
                continue;
            }

            addressBook.addMeeting(meeting);
        }

        return addressBook;
    }
}
