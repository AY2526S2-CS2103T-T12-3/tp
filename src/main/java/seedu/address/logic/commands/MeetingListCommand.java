package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all meetings in the address book to the user.
 */
public class MeetingListCommand extends Command {

    public static final String COMMAND_WORD = "meetinglist";

    public static final String MESSAGE_SUCCESS = "Listed all meetings in the Meetings tab";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.getAddressBook().getMeetingList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
