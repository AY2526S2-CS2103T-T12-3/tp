package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {
    @Test
    public void format_personWithAndWithoutPhoneEmail_correctlyFormats() {

        // Person with both phone and email
        String formattedFull = Messages.format(BOB);
        assertTrue(formattedFull.contains(BOB.getName().toString()));
        assertTrue(formattedFull.contains("Phone: " + VALID_PHONE_BOB));
        assertTrue(formattedFull.contains("Email: " + VALID_EMAIL_BOB));
        assertTrue(formattedFull.contains("Tags:"));

        // Person without phone
        Person personNoPhone = new PersonBuilder(BOB)
                .withPhone(null)
                .build();
        String formattedNoPhone = Messages.format(personNoPhone);
        assertTrue(formattedNoPhone.contains(VALID_NAME_BOB));
        assertFalse(formattedNoPhone.contains("Phone:"));
        assertTrue(formattedNoPhone.contains(VALID_EMAIL_BOB));

        // Person without email
        Person personNoEmail = new PersonBuilder(BOB)
                .withEmail(null)
                .build();
        String formattedNoEmail = Messages.format(personNoEmail);
        assertTrue(formattedNoEmail.contains(VALID_NAME_BOB));
        assertTrue(formattedNoEmail.contains("Phone: " + VALID_PHONE_BOB));
        assertFalse(formattedNoEmail.contains("Email:"));
    }
}
