package seedu.address.ui.commandhistory;

import java.util.ArrayList;

/**
 * A list to store previously sent commands.
 * This command history works similarly to a conventional terminal's command history.
 * Commands can be added to the history using {@code add()}. The {@code CommandHistory} will keep track of the
 * current command using an internal pointer which can be moved around using {@code nextCommand()} and
 * {@code prevCommand()}. Recorded commands will be added to the end of the list,
 * and duplicate commands won't be added twice in a row.<br><br>
 *
 * Example usage:
 * <pre>
 * {@code
 * CommandHistory ch = new CommandHistory();
 * ch.add("first command");
 * ch.add("second command");
 *
 * String userDraft = "draft command"; // Assume the user is currently editing userDraft.
 * userDraft = ch.prevCommand(userDraft); // userDraft becomes "second command".
 * userDraft = ch.prevCommand(userDraft); // userDraft becomes "first command".
 * userDraft = ch.nextCommand(userDraft); // userDraft becomes "second command".
 * userDraft = ch.nextCommand(userDraft); // userDraft becomes "draft command".
 * }
 * </pre>
 */
public class CommandHistory {
    private final ArrayList<String> commands;
    private int currentIndex;
    private String draft;

    /**
     * Initializes an empty command history.
     */
    public CommandHistory() {
        this.commands = new ArrayList<>();
        resetIndexAndDraft();
    }

    /**
     * Adds a command to the command history. Duplicate commands won't be added twice in a row.
     * When adding a command, the current command position will be reset to the end of the list,
     * i.e. After adding a command, <code>prevCommand()</code> will return the last added command.
     * Blank or null inputs will throw an exception.
     * @param command the command.
     * @throws IllegalArgumentException if the command string is blank or null.
     */
    public void add(String command) throws IllegalArgumentException {
        if (command == null) {
            throw new IllegalArgumentException("Command history cannot accept null commands");
        }
        if (command.isBlank()) {
            throw new IllegalArgumentException("Adding blank commands to command history is not allowed");
        }

        if (commands.isEmpty() || !commands.get(commands.size() - 1).equals(command)) {
            // Duplicate commands won't be added twice in a row.
            commands.add(command);
        }
        resetIndexAndDraft();
    }

    /**
     * Shifts to the next command in the history, or the draft if the end of history is reached.
     * @param currentCommand the current command that is being edited by the user. Used to save as draft.
     * @return the next command in the history.
     */
    public String nextCommand(String currentCommand) {
        if (isOnDraft()) {
            // If the user is currently editing the draft, then nextCommand() shouldn't erase the draft.
            return currentCommand;
        }
        currentIndex++; // isOnLatestCommand() already ensures that currentIndex < commands.size().
        return get(currentIndex);
    }

    /**
     * Shifts to the previous command in the history, or the draft if the history is empty.
     * If the history is currently at the oldest command, then this function returns the input back to the caller.
     * @param currentCommand the current command that is being edited by the user. Used to save as draft.
     * @return the previous command in the history.
     */
    public String prevCommand(String currentCommand) throws IllegalArgumentException {
        if (isOnDraft()) {
            draft = currentCommand; // Saves the current draft.
        }

        // If currently on the oldest command, then prevCommand() should do nothing.
        if (isOnOldestCommand()) {
            return currentCommand;
        }

        if (currentIndex > 0) {
            currentIndex--;
        }
        return get(currentIndex);
    }

    /**
     * Returns true if <code>currentIndex == 0</code>. In other words, returns true if the current
     * index is referring to the first command in the history.
     */
    public boolean isOnOldestCommand() {
        return currentIndex == 0;
    }

    /**
     * Returns true if <code>currentIndex == commands.size()</code>. In other words, returns true if the current
     * index is referring to the draft.
     */
    public boolean isOnDraft() {
        return currentIndex == commands.size();
    }

    public boolean isEmpty() {
        return this.commands.isEmpty();
    }

    public int size() {
        return this.commands.size();
    }

    /**
     * Returns the command at index. If <code>i == commands.size()</code>, returns the draft.
     * @param i the index of the command.
     * @return the command at position <code>i</code>.
     */
    private String get(int i) {
        if (i == commands.size() || commands.isEmpty()) {
            return draft;
        }
        return commands.get(i);
    }

    /**
     * Resets the index to <code>commands.size()</code> and empties the draft.
     */
    private void resetIndexAndDraft() {
        currentIndex = commands.size();
        this.draft = "";
    }
}
