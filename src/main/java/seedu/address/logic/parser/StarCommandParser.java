package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.StarCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new StarCommand object
 */
public class StarCommandParser implements Parser<StarCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * StarCommand
     * and returns a StarCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public StarCommand parse(String args) throws ParseException {
        try {
            Set<Index> indices = ParserUtil.parseIndices(args, StarCommand.MESSAGE_USAGE);
            return new StarCommand(indices);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, StarCommand.MESSAGE_USAGE), pe);
        }
    }

}
