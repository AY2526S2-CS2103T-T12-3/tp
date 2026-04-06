package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {
    public static final String MESSAGE_INVALID_PERSON =
            "Invalid person at index %d: %s, skipping person.";
    public static final String MESSAGE_INVALID_MEETING =
            "Invalid meeting at index %d: %s, skipping meeting.";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "%s already exists in the address book, skipping person.";
    public static final String MESSAGE_DUPLICATE_MEETING =
            "%s already exists in the meeting list, skipping meeting.";
    public static final String MESSAGE_DUPLICATE_ID =
            "%s has an ID that already exists in the address book, skipping person.";
    public static final String MESSAGE_MEETING_WITH_INVALID_PARTICIPANT =
            "Meeting \"%s\" has invalid participant ID \"%s\", skipping meeting.";

    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

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
     * Converts this JSON-serializable address book into the model's {@code AddressBook} object.
     * Logs any duplicates or invalid entries and skips them.
     */
    public AddressBook toModelType() {
        AddressBook addressBook = new AddressBook();

        // Add persons safely
        addPersonsToModel(addressBook);

        // Add meetings safely
        addMeetingsToModel(addressBook);

        return addressBook;
    }

    /**
     * Tries to add all persons from the JSON list to the given {@code AddressBook}.
     * Skips and logs duplicates or invalid entries.
     */
    private void addPersonsToModel(AddressBook addressBook) {
        for (int i = 0; i < persons.size(); i++) {
            JsonAdaptedPerson jsonAdaptedPerson = persons.get(i);
            try {
                Person person = jsonAdaptedPerson.toModelType();

                // Skip exact duplicates
                if (addressBook.hasPerson(person)) {
                    logger.warning(String.format(MESSAGE_DUPLICATE_PERSON, person.getName()));
                    continue;
                }

                // Skip duplicate IDs
                if (addressBook.hasSameID(person)) {
                    logger.warning(String.format(MESSAGE_DUPLICATE_ID, person.getName()));
                    continue;
                }

                addressBook.addPerson(person);
            } catch (IllegalValueException | IllegalArgumentException e) {
                // Log invalid person with index and exception message
                logger.warning(String.format(MESSAGE_INVALID_PERSON, i, e.getMessage()));
            }
        }
    }

    /**
     * Tries to add all meetings from the JSON list to the given {@code AddressBook}.
     * Skips and logs duplicates, invalid entries, and meetings with invalid participants.
     */
    private void addMeetingsToModel(AddressBook addressBook) {
        for (int i = 0; i < meetings.size(); i++) {
            JsonAdaptedMeeting jsonAdaptedMeeting = meetings.get(i);
            try {
                Meeting meeting = jsonAdaptedMeeting.toModelType();

                // Skip duplicate meetings
                if (addressBook.hasMeeting(meeting)) {
                    logger.warning(String.format(MESSAGE_DUPLICATE_MEETING, meeting.toString()));
                    continue;
                }

                // Skip the entire meeting if any participant is not in contacts.
                if (hasAnyParticipantNotInContacts(meeting, addressBook)) {
                    continue;
                }

                addressBook.addMeeting(meeting);
            } catch (IllegalValueException | IllegalArgumentException e) {
                // Log invalid meeting with index and exception message
                logger.warning(String.format(MESSAGE_INVALID_MEETING, i, e.getMessage()));
            }
        }
    }

    /**
     * Returns true if the meeting has any participants not in the {@code UniquePersonList}.
     * Logs a warning if such a participant is found and immediately returns true.
     */
    private boolean hasAnyParticipantNotInContacts(Meeting meeting, AddressBook addressBook) {
        for (PersonId participantId : meeting.getParticipantsIDs()) {
            if (!addressBook.hasSameID(participantId)) {
                logger.warning(String.format(
                        MESSAGE_MEETING_WITH_INVALID_PARTICIPANT,
                        meeting.toString(), participantId.toString()));
                return true; // Found an invalid participant, stop checking further
            }
        }
        return false; // All participants are valid
    }
}
