package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void updateFilteredPersonListStacked_filtersCorrectly() {
        ModelManager model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // first filter: name contains "meier"
        Predicate<Person> first = person ->
                person.getName().fullName.toLowerCase().contains("meier");

        model.updateFilteredPersonList(first);

        assertEquals(2, model.getFilteredPersonList().size()); // Benson Meier, Daniel Meier
        // second filter: email contains "cornelia"
        Predicate<Person> second = person ->
                person.getEmail().value.toLowerCase().contains("cornelia");

        model.updateFilteredPersonListStacked(second);

        // should only be Daniel Meier
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(model.getFilteredPersonList().get(0).getName().fullName.toLowerCase(),
                "daniel meier");
    }

    @Test
    public void updateFilteredPersonListStacked_noExistingPredicate_works() {
        ModelManager model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Predicate<Person> predicate = person -> person.getName().fullName.toLowerCase().contains("meier");

        model.updateFilteredPersonListStacked(predicate);

        assertEquals(2, model.getFilteredPersonList().size());
    }

    @Test
    public void updateFilteredPersonList_afterStacked_resetsProperly() {
        ModelManager model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Predicate<Person> meier = p -> p.getName().fullName.toLowerCase().contains("meier");
        Predicate<Person> dan = p -> p.getName().fullName.toLowerCase().contains("dan");

        model.updateFilteredPersonList(meier);
        model.updateFilteredPersonListStacked(dan);

        // now reset
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertEquals(model.getAddressBook().getPersonList().size(),
                model.getFilteredPersonList().size());
    }

    @Test
    public void updateFilteredPersonListIncremental_appliesAndLogic() {
        ModelManager model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Predicate<Person> meier = p -> p.getName().fullName.contains("Meier");
        Predicate<Person> pauline = p -> p.getName().fullName.contains("pauline");

        model.updateFilteredPersonList(meier);
        model.updateFilteredPersonListStacked(pauline);

        // should be empty (no one is both Tan and Lee)
        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(
                new PersonMatchesKeywordsPredicate(
                        List.of(), // globalKeywords
                        Arrays.asList(keywords), // nameKeywords
                        List.of(), // phoneKeywords
                        List.of() // emailKeywords
                ));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
