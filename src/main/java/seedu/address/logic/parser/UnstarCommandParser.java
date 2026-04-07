package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnstarCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnstarCommand object
 */
public class UnstarCommandParser implements Parser<UnstarCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * UnstarCommand
     * and returns a UnstarCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public UnstarCommand parse(String args) throws ParseException {
        try {
            Set<Index> indices = ParserUtil.parseIndices(args, UnstarCommand.MESSAGE_USAGE);
            return new UnstarCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnstarCommand.MESSAGE_USAGE), pe);
        }
    }

}
