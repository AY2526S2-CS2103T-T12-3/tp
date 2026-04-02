package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    private static final PersonId ID_1 = new PersonId("00000000-0000-0000-0000-000000000001");
    private static final PersonId ID_2 = new PersonId("00000000-0000-0000-0000-000000000002");
    private static final PersonId ID_3 = new PersonId("00000000-0000-0000-0000-000000000003");
    private static final PersonId ID_4 = new PersonId("00000000-0000-0000-0000-000000000004");
    private static final PersonId ID_5 = new PersonId("00000000-0000-0000-0000-000000000005");
    private static final PersonId ID_6 = new PersonId("00000000-0000-0000-0000-000000000006");

    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(
                    ID_1,
                    new Name("Alex Yeoh"),
                    new Phone("87438807"),
                    new Email("alexyeoh@example.com"),
                    getTagSet("friends")
            ),
            new Person(
                    ID_2,
                    new Name("Bernice Yu"),
                    new Phone("99272758"),
                    new Email("berniceyu@example.com"),
                    getTagSet("colleagues", "friends")
            ),
            new Person(
                    ID_3,
                    new Name("Charlotte Oliveiro"),
                    new Phone("93210283"),
                    new Email("charlotte@example.com"),
                    getTagSet("neighbours")
            ),
            new Person(
                    ID_4,
                    new Name("David Li"),
                    new Phone("91031282"),
                    new Email("lidavid@example.com"),
                    getTagSet("family")
            ),
            new Person(
                    ID_5,
                    new Name("Irfan Ibrahim"),
                    new Phone("92492021"),
                    new Email("irfan@example.com"),
                    getTagSet("classmates")
            ),
            new Person(
                    ID_6,
                    new Name("Roy Balakrishnan"),
                    new Phone("92624417"),
                    new Email("royb@example.com"),
                    getTagSet("colleagues")
            )
        };
    }

    public static Meeting[] getSampleMeetings() {
        return new Meeting[] {
            new Meeting(
                    new Description("Project Meeting"),
                    new MeetingDate("2026-06-15"),
                    Set.of(ID_1, ID_3, ID_4)),
            new Meeting(
                    new Description("Coffee"),
                    new MeetingDate("2026-06-11"),
                    Set.of(ID_1))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Meeting sampleMeeting : getSampleMeetings()) {
            sampleAb.addMeeting(sampleMeeting);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}
