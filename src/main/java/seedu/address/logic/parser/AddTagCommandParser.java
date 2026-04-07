package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEPARATOR;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddTagCommand object
 */
public class AddTagCommandParser implements Parser<AddTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddTagCommand
     * and returns an AddTagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SEPARATOR);

        Set<Index> indices = ParserUtil.parseIndices(argMultimap.getPreamble(), AddTagCommand.MESSAGE_USAGE);

        Set<Tag> tags = new HashSet<>();
        ParserUtil.parseTagsOptional(argMultimap.getAllValues(PREFIX_SEPARATOR)).ifPresent(tags::addAll);

        if (tags.isEmpty()) {
            throw new ParseException(AddTagCommand.MESSAGE_NO_TAGS);
        }

        return new AddTagCommand(indices, tags);
    }
}
