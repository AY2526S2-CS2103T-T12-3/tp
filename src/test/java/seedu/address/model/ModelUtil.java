package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import seedu.address.model.person.Person;

/**
 * Utility class for comparing two models
 */
public class ModelUtil {

    /**
     * Compares two models to see if they are similar in persons,
     * ignoring the ID that is randomly generated
     */
    public static void assertModelPersonsEqual(Model expected, Model actual) {
        List<Person> expectedList = expected.getAddressBook().getPersonList();
        List<Person> actualList = actual.getAddressBook().getPersonList();

        assertEquals(expectedList.size(), actualList.size());

        for (int i = 0; i < expectedList.size(); i++) {
            Person expectedPerson = expectedList.get(i);
            Person actualPerson = actualList.get(i);

            assertTrue(expectedPerson.equals(actualPerson));
        }
    }
}
