package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEWTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OLDTAG;
import static seedu.address.logic.parser.ParserUtil.isPrefixPresent;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditTagCommand object.
 */
public class EditTagCommandParser implements Parser<EditTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditTagCommand
     * and returns a EditTagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public EditTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_OLDTAG, PREFIX_NEWTAG);

        boolean isOldTagEmpty = !isPrefixPresent(argMultimap, PREFIX_OLDTAG);
        boolean isNewTagEmpty = !isPrefixPresent(argMultimap, PREFIX_NEWTAG);
        boolean isPreamblePresent = !argMultimap.getPreamble().isEmpty();

        if (isOldTagEmpty || isNewTagEmpty || !isPreamblePresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_OLDTAG, PREFIX_NEWTAG);

        Tag oldTag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_OLDTAG).get());
        Tag newTag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_NEWTAG).get());

        String[] indices = argMultimap.getPreamble().split(",");
        Set<Index> targetIndices = new HashSet<>();

        if (argMultimap.getPreamble().trim().equals("all")) {
            // global edit, do not add anything to targetIndices yet, EditTagCommand handles this
        } else {
            for (String indexStr : indices) {
                Index parsedIndex = ParserUtil.parseIndex(indexStr);
                targetIndices.add(parsedIndex);
            }
        }

        return new EditTagCommand(targetIndices, oldTag, newTag);
    }
}
