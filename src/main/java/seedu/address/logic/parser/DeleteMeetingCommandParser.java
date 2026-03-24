package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING;

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

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_MEETING);

        boolean isIndexMissing = argMultimap.getPreamble().isEmpty();
        boolean areMeetingsMissing = !isPrefixPresent(argMultimap, PREFIX_MEETING);

        if (isIndexMissing || areMeetingsMissing) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteMeetingCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MEETING);

        // Parse indices
        Set<Index> indices = ParserUtil.parseIndices(
                argMultimap.getPreamble(),
                DeleteMeetingCommand.MESSAGE_USAGE);

        // Parse meeting indices
        Set<Index> meetingIndices = ParserUtil.parseIndices(
                argMultimap.getValue(PREFIX_MEETING).get().trim(),
                DeleteMeetingCommand.MESSAGE_USAGE);
        return new DeleteMeetingCommand(indices, meetingIndices);
    }

    /**
     * Returns true if the prefix contains a value in the given ArgumentMultimap.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }
}
