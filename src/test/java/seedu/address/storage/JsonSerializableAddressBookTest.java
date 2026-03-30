package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.PROJECT_MEETING;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");

    private static final Path TYPICAL_PERSONS_FILE =
            TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");

    private static final Path INVALID_PERSON_FILE =
            TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");

    private static final Path DUPLICATE_PERSON_FILE =
            TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");

    private static final Path DUPLICATE_MEETING_FILE =
            TEST_DATA_FOLDER.resolve("duplicateMeetingAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TYPICAL_PERSONS_FILE, JsonSerializableAddressBook.class).get();

        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();

        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_PERSON_FILE, JsonSerializableAddressBook.class).get();

        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersonsIgnored_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                DUPLICATE_PERSON_FILE, JsonSerializableAddressBook.class).get();

        AddressBook result = dataFromFile.toModelType();

        assertEquals(1, result.getPersonList().size());
        Person personWithDuplicate = result.getPersonList().get(0);

        assertEquals(ALICE, personWithDuplicate);
    }

    @Test
    public void toModelType_duplicateMeetingsIgnored_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                DUPLICATE_MEETING_FILE, JsonSerializableAddressBook.class).get();

        AddressBook result = assertDoesNotThrow(dataFromFile::toModelType);

        // Only one meeting should remain
        assertEquals(1, result.getMeetingList().size());
    }

    @Test
    public void toModelType_duplicateMeetingsIgnored_correctMeetingKept() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                DUPLICATE_MEETING_FILE, JsonSerializableAddressBook.class).get();

        AddressBook result = dataFromFile.toModelType();

        assertEquals(1, result.getMeetingList().size());

        Meeting meetingWithDuplicate = result.getMeetingList().get(0);

        assertEquals(PROJECT_MEETING.getDescription(), meetingWithDuplicate.getDescription());
        assertEquals(LocalDate.parse(PROJECT_MEETING.getDate().toString()), meetingWithDuplicate.getDate());
    }
}
