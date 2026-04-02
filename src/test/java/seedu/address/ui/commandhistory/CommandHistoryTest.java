package seedu.address.ui.commandhistory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {
    private static final String USER_DRAFT = "Editing 1";
    private static final String USER_DRAFT_2 = "Editing 2";
    private static final String COMMAND_1 = "Test command 1";
    private static final String COMMAND_2 = "Test command 2";
    private static final String COMMAND_3 = "Test command 3";

    private static final String BLANK_STRING = "";

    private static CommandHistory typicalCommandHistory() {
        CommandHistory ch = new CommandHistory();
        ch.add(COMMAND_1);
        ch.add(COMMAND_2);
        ch.add(COMMAND_3);

        return ch;
    }

    @Test
    public void checkEmptyAndIndex_emptyHistory_success() {
        CommandHistory ch = new CommandHistory();

        assertTrue(ch.isEmpty());
        assertTrue(ch.isOnDraft());
        assertTrue(ch.isOnOldestCommand());
    }

    @Test
    public void checkEmptyAndIndex_oneCommand_success() {
        CommandHistory ch = new CommandHistory();
        ch.add(COMMAND_1);

        assertFalse(ch.isEmpty());
        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());
    }

    @Test
    public void checkIndex_twoCommands_isOnDraft() {
        CommandHistory ch = new CommandHistory();
        ch.add(COMMAND_1);
        ch.add(COMMAND_2);

        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());
    }

    @Test
    public void addCommand_duplicateCommands_duplicateIgnored() {
        CommandHistory ch = new CommandHistory();
        ch.add(COMMAND_1);
        ch.add(COMMAND_1);
        ch.add(COMMAND_1);

        assertEquals(1, ch.size());
    }

    @Test
    public void addCommand_duplicateNotInARow_allAdded() {
        CommandHistory ch = new CommandHistory();
        ch.add(COMMAND_1);
        ch.add(COMMAND_2); // Alternate between commands 1 and 2.
        ch.add(COMMAND_1);

        assertEquals(3, ch.size());
    }

    @Test
    public void addCommand_null_fail() {
        CommandHistory ch = new CommandHistory();
        assertThrows(IllegalArgumentException.class, () -> ch.add(null));
    }

    @Test
    public void addCommand_blank_fail() {
        CommandHistory ch = new CommandHistory();
        assertThrows(IllegalArgumentException.class, () -> ch.add(BLANK_STRING));
    }

    @Test
    public void nextCommand_emptyHistory_returnsDraft() {
        CommandHistory ch = new CommandHistory();

        String userDraft = USER_DRAFT;
        userDraft = ch.nextCommand(userDraft);

        assertEquals(USER_DRAFT, userDraft);
    }

    @Test
    public void prevCommand_emptyHistory_returnsDraft() {
        CommandHistory ch = new CommandHistory();

        String userDraft = USER_DRAFT;
        userDraft = ch.prevCommand(userDraft);

        assertEquals(USER_DRAFT, userDraft);
    }

    @Test
    public void nextCommand_typicalHistoryAtDraft_returnsDraft() {
        CommandHistory ch = typicalCommandHistory();
        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());

        String userDraft = USER_DRAFT;

        userDraft = ch.nextCommand(userDraft);
        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());
        assertEquals(USER_DRAFT, userDraft);

        // Do twice just for good measure.
        userDraft = ch.nextCommand(userDraft);
        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());
        assertEquals(USER_DRAFT, userDraft);
    }

    @Test
    public void prevCommand_typicalHistory_success() {
        CommandHistory ch = typicalCommandHistory();
        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());

        String userDraft = USER_DRAFT;

        userDraft = ch.prevCommand(userDraft);
        assertFalse(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());
        assertEquals(COMMAND_3, userDraft);

        userDraft = ch.prevCommand(userDraft);
        assertFalse(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());
        assertEquals(COMMAND_2, userDraft);

        // Reach the oldest command.
        userDraft = ch.prevCommand(userDraft);
        assertFalse(ch.isOnDraft());
        assertTrue(ch.isOnOldestCommand());
        assertEquals(COMMAND_1, userDraft);

        // Once oldest command is reached, prevCommand() would stay at it.
        userDraft = ch.prevCommand(userDraft);
        assertFalse(ch.isOnDraft());
        assertTrue(ch.isOnOldestCommand());
        assertEquals(COMMAND_1, userDraft);
    }

    @Test
    public void navigateCommand_typicalHistory_success() {
        CommandHistory ch = typicalCommandHistory();
        assertTrue(ch.isOnDraft());
        assertFalse(ch.isOnOldestCommand());

        String userDraft = USER_DRAFT;

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_3, userDraft);

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_2, userDraft);

        userDraft = ch.nextCommand(userDraft);
        assertEquals(COMMAND_3, userDraft);

        userDraft = ch.nextCommand(userDraft);
        assertEquals(USER_DRAFT, userDraft);

        // Once draft is reached, nextCommand() should stay at it.
        userDraft = ch.nextCommand(userDraft);
        assertEquals(USER_DRAFT, userDraft);

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_3, userDraft);

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_2, userDraft);

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_1, userDraft);

        // Once oldest command is reached, prevCommand() should stay at it.
        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_1, userDraft);
    }

    @Test
    public void navigateCommand_changeDraftInTheMiddle_originalDraftKept() {
        CommandHistory ch = typicalCommandHistory();

        String userDraft = USER_DRAFT;

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_3, userDraft);

        userDraft = USER_DRAFT_2; // Simulate a user editing one of the previous commands.

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_2, userDraft);

        userDraft = ch.nextCommand(userDraft);
        assertEquals(COMMAND_3, userDraft); // COMMAND_3 shouldn't be overwritten.

        userDraft = ch.nextCommand(userDraft);
        assertEquals(USER_DRAFT, userDraft); // Original user draft shouldn't be overwritten.
    }

    @Test
    public void navigateCommand_changeDraft_success() {
        CommandHistory ch = typicalCommandHistory();

        String userDraft = USER_DRAFT;

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_3, userDraft);

        ch.nextCommand(userDraft);

        userDraft = USER_DRAFT_2; // Simulate a user editing the current draft.

        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_3, userDraft);

        userDraft = ch.nextCommand(userDraft);
        assertEquals(USER_DRAFT_2, userDraft); // Original user draft shouldn't be overwritten.
    }

    @Test
    public void navigateCommand_changeDraftDuringOldest_draftKept() {
        CommandHistory ch = typicalCommandHistory();

        String userDraft = USER_DRAFT;

        // Go to the oldest command
        userDraft = ch.prevCommand(userDraft);
        userDraft = ch.prevCommand(userDraft);
        userDraft = ch.prevCommand(userDraft);
        assertEquals(COMMAND_1, userDraft);

        userDraft = USER_DRAFT_2;
        userDraft = ch.prevCommand(userDraft);
        assertEquals(USER_DRAFT_2, userDraft);
    }
}
