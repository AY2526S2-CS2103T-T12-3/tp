package seedu.address.logic.parser;

import static seedu.address.logic.Messages.CONTACT_TYPE;

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
     * @throws ParseException If the user input does not conform the expected format.
     */
    public UnstarCommand parse(String args) throws ParseException {
        Set<Index> indices = ParserUtil.parseIndices(args, CONTACT_TYPE, UnstarCommand.MESSAGE_USAGE);
        return new UnstarCommand(indices);
    }

}
