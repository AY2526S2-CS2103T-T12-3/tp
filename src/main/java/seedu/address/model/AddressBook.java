package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.UniqueMeetingList;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueMeetingList meetings;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        meetings = new UniqueMeetingList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //=========== List overwriting operations ===========================================================

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the meeting list with {@code meetings}.
     * {@code meetings} must not contain duplicate meetings.
     */
    public void setMeetings(List<Meeting> meetings) {
        this.meetings.setMeetings(meetings);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setMeetings(newData.getMeetingList());
    }

    //=========== Person-level operations ===========================================================

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the same {@code ID} as {@code person} exists in the address book.
     */
    public boolean hasSameID(Person person) {
        requireNonNull(person);
        return persons.hasSameID(person);
    }

    /**
     * Returns true if a person with the same {@code PersonId} exists in the address book.
     */
    public boolean hasSameID(PersonId id) {
        requireNonNull(id);
        return persons.hasSameID(id);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     * If the edited person is a participant of a meeting, then the changes will also be reflected in those
     * meetings (i.e. if the ID of the person is changed).
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        // Updates references to this person from meetings
        for (Meeting m : getMeetingsContainingId(target.getId())) {
            Set<PersonId> newPersonIdSet = m.getParticipantsIDs();

            newPersonIdSet.remove(target.getId());
            newPersonIdSet.add(editedPerson.getId());

            Meeting editedMeeting = new Meeting(m.getDescription(), m.getDate(), newPersonIdSet);
            setMeeting(m, editedMeeting);
        }

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in the address book.
     * If the deleted person is a participant of a meeting(s), then their ID will also be removed from those meetings.
     */
    public void removePerson(Person key) {
        // Removes references to this person from meetings
        for (Meeting m : getMeetingsContainingId(key.getId())) {
            Meeting editedMeeting = removeIdFromMeeting(m, key.getId());
            setMeeting(m, editedMeeting);
        }

        persons.remove(key);
    }

    /**
     *  Returns the person with the given PersonId. Throws {@code PersonNotFoundException} if not found.
     */
    public Person getPerson(PersonId id) throws PersonNotFoundException {
        return persons.asUnmodifiableObservableList()
                .stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElseThrow(PersonNotFoundException::new);
    }

    //=========== Meeting-level operations ===========================================================

    /**
     * Returns true if a meeting with the same details as {@code meeting} exists in the address book.
     */
    public boolean hasMeeting(Meeting meeting) {
        requireNonNull(meeting);
        return meetings.contains(meeting);
    }

    /**
     * Adds a meeting to the address book.
     * The meeting must not already exist in the address book.
     */
    public void addMeeting(Meeting m) {
        verifyParticipantsExist(m);
        meetings.add(m);
    }

    /**
     * Replaces the given meeting {@code target} in the list with {@code editedMeeting}.
     * {@code target} must exist in the address book. The meeting details of {@code editedMeeting}
     * must not be the same as another existing meeting in the address book.
     */
    public void setMeeting(Meeting target, Meeting editedMeeting) {
        requireNonNull(editedMeeting);
        verifyParticipantsExist(editedMeeting);

        meetings.setMeeting(target, editedMeeting);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeMeeting(Meeting key) {
        meetings.remove(key);
    }

    //=========== Util methods ===========================================================

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("meetings", meetings)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Meeting> getMeetingList() {
        return meetings.asUnmodifiableObservableList();
    }

    /**
     * Throws an error if any of the ID in the meeting's participant set does not exist in the address book.
     */
    private void verifyParticipantsExist(Meeting meeting) throws PersonNotFoundException {
        for (PersonId id : meeting.getParticipantsIDs()) {
            getPerson(id);
        }
    }

    /**
     * Returns the set of all {@code Meeting}s in this address book
     * whose participant set contains the given {@code id}.
     */
    private Set<Meeting> getMeetingsContainingId(PersonId id) {
        return meetings.asUnmodifiableObservableList().stream()
                .filter(meeting -> meeting.getParticipantsIDs().contains(id))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a copy of the given {@code meeting}
     * with the specified {@code id} removed from the set of participants PersonId.
     */
    private static Meeting removeIdFromMeeting(Meeting meeting, PersonId id) {
        Set<PersonId> newPersonIdSet = meeting.getParticipantsIDs();
        newPersonIdSet.remove(id);
        return new Meeting(meeting.getDescription(), meeting.getDate(), newPersonIdSet);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons) && meetings.equals(otherAddressBook.meetings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, meetings);
    }
}
