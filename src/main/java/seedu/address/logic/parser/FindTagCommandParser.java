package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEPARATOR;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindTagCommand object
 */
public class FindTagCommandParser implements Parser<FindTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindTagCommand
     * and returns a FindTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SEPARATOR);

        Set<Tag> tags = new HashSet<>();
        ParserUtil.parseTagsOptional(argMultimap.getAllValues(PREFIX_SEPARATOR)).ifPresent(tags::addAll);

        if (tags.isEmpty()) {
            throw new ParseException(FindTagCommand.MESSAGE_NO_TAGS);
        }

        if (!argMultimap.getPreamble().isEmpty()) {
            // invalid format as there should be nothing before the first slash
            throw new ParseException("Error: Format is invalid.\n" + FindTagCommand.MESSAGE_FORMAT);
        }


        return new FindTagCommand(tags);
    }
}

