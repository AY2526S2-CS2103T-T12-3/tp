package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalMeetings.getTypicalMeetings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import seedu.address.model.AddressBook;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {
    public static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    public static final UUID UUID_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    public static final UUID UUID_4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
    public static final UUID UUID_5 = UUID.fromString("00000000-0000-0000-0000-000000000005");
    public static final UUID UUID_6 = UUID.fromString("00000000-0000-0000-0000-000000000006");

    // Assign unique IDs for each person
    public static final Person ALICE = new PersonBuilder()
            .withId(UUID_1)
            .withName("Alice Pauline")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends")
            .build();

    public static final Person BENSON = new PersonBuilder()
            .withId(UUID_2)
            .withName("Benson Meier")
            .withEmail("johnd@example.com")
            .withPhone("98765432")
            .withTags("owesMoney", "friends")
            .build();

    public static final Person CARL = new PersonBuilder()
            .withId(UUID_3)
            .withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("heinz@example.com")
            .build();

    public static final Person DANIEL = new PersonBuilder()
            .withId(UUID_4)
            .withName("Daniel Meier")
            .withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withTags("friends")
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder()
            .withId(UUID_5)
            .withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withTags(VALID_TAG_FRIEND)
            .build();

    public static final Person BOB = new PersonBuilder()
            .withId(UUID_6)
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons and meetings.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        for (Meeting meeting : getTypicalMeetings()) {
            ab.addMeeting(meeting);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL));
    }
}
