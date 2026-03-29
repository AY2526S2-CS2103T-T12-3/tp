package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalMeetings.COFFEE_MEETING;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.exceptions.DuplicateMeetingException;
import seedu.address.model.meeting.exceptions.MeetingNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE).withTags(CommandTestUtil.VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(TypicalPersons.ALICE, editedAlice);
        List<Meeting> emptyMeetings = new ArrayList<>();
        AddressBookStub newData = new AddressBookStub(newPersons, emptyMeetings);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateMeeting_throwsDuplicateMeetingException() {
        List<Person> newPersons = TypicalPersons.getTypicalPersons();

        List<Meeting> newMeetings = new ArrayList<>();
        newMeetings.add(new Meeting(
                COFFEE_MEETING.getDescription(),
                COFFEE_MEETING.getDate(),
                COFFEE_MEETING.getParticipantsID()));
        newMeetings.add(new Meeting(
                COFFEE_MEETING.getDescription(),
                COFFEE_MEETING.getDate(),
                COFFEE_MEETING.getParticipantsID()));

        AddressBookStub newData = new AddressBookStub(newPersons, newMeetings);

        assertThrows(DuplicateMeetingException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateMeetingDifferentParticipants_throwsDuplicateMeetingException() {
        List<Person> newPersons = TypicalPersons.getTypicalPersons();

        List<Meeting> newMeetings = new ArrayList<>();
        newMeetings.add(new Meeting(
                COFFEE_MEETING.getDescription(),
                COFFEE_MEETING.getDate(),
                COFFEE_MEETING.getParticipantsID()));

        Set<UUID> participantsMinusOne = COFFEE_MEETING.getParticipantsID();
        participantsMinusOne.remove(participantsMinusOne.iterator().next()); // Remove first UUID.
        newMeetings.add(new Meeting(
                COFFEE_MEETING.getDescription(),
                COFFEE_MEETING.getDate(),
                participantsMinusOne));

        AddressBookStub newData = new AddressBookStub(newPersons, newMeetings);

        assertThrows(DuplicateMeetingException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(TypicalPersons.ALICE);
        assertTrue(addressBook.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(TypicalPersons.ALICE);
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE).withTags(CommandTestUtil.VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void getPerson_validUuid_success() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);

        Person firstPerson = addressBook.getPersonList().stream().findFirst().orElseThrow(PersonNotFoundException::new);
        UUID idToFind = firstPerson.getId();

        assertEquals(firstPerson, addressBook.getPerson(idToFind));
    }

    @Test
    public void addMeeting_nonexistentUuid_fail() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);

        Person firstPerson = addressBook.getPersonList().stream().findFirst().orElseThrow(PersonNotFoundException::new);

        addressBook.removePerson(firstPerson);

        Meeting meetingWithNonexistentPerson = new Meeting(
                "Meetup",
                LocalDate.of(2026, 7, 5),
                Set.of(firstPerson.getId()));

        assertThrows(PersonNotFoundException.class, () -> addressBook.addMeeting(meetingWithNonexistentPerson));
    }

    @Test
    public void removePerson_partOfMeeting_participantIdRemoved() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);

        Meeting firstMeeting =
                addressBook.getMeetingList().stream().findFirst().orElseThrow(MeetingNotFoundException::new);
        UUID firstParticipantId = firstMeeting.getParticipantsID().iterator().next();

        addressBook.removePerson(addressBook.getPerson(firstParticipantId));

        Set<UUID> newUuidSet = firstMeeting.getParticipantsID();
        newUuidSet.remove(firstParticipantId);

        Meeting editedMeeting = new Meeting(firstMeeting.getDescription(), firstMeeting.getDate(), newUuidSet);

        for (Meeting m : addressBook.getMeetingList()) {
            if (m.isSameMeeting(editedMeeting)) {
                assertEquals(editedMeeting.getParticipantsID(), m.getParticipantsID());
            }
        }
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{persons=" + addressBook.getPersonList() + ", "
                + "meetings=" + addressBook.getMeetingList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Meeting> meetings = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<Meeting> meetings) {
            this.persons.setAll(persons);
            this.meetings.setAll(meetings);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Meeting> getMeetingList() {
            return meetings;
        }
    }

}
