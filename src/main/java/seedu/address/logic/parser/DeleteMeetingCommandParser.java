package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteMeetingCommand object.
 */

public class DeleteMeetingCommandParser implements Parser<DeleteMeetingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteMeetingCommand
     * and returns an DeleteMeetingCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DeleteMeetingCommand parse(String args) throws ParseException {

        boolean areIndexesMissing = args.isEmpty();

        if (areIndexesMissing) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteMeetingCommand.MESSAGE_USAGE));
        }

        // Parse meeting indices
        Set<Index> meetingIndices = ParserUtil.parseIndices(
                args,
                DeleteMeetingCommand.MESSAGE_USAGE);

        return new DeleteMeetingCommand(meetingIndices);
    }
}
