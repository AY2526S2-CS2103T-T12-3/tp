package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEWTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OLDTAG;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditTagCommand object
 */
public class EditTagCommandParser implements Parser<EditTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditTagCommand
     * and returns a EditTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_OLDTAG, PREFIX_NEWTAG);

        boolean isOldTagEmpty = !isPrefixPresent(argMultimap, PREFIX_OLDTAG);
        boolean isNewTagEmpty = !isPrefixPresent(argMultimap, PREFIX_NEWTAG);
        boolean isPreamblePresent = !argMultimap.getPreamble().isEmpty();

        if (isOldTagEmpty || isNewTagEmpty || !isPreamblePresent) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_FORMAT));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_OLDTAG, PREFIX_NEWTAG);

        Tag oldTag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_OLDTAG).get());
        Tag newTag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_NEWTAG).get());

        String[] indices = argMultimap.getPreamble().split(",");
        List<Index> targetIndices = new ArrayList<>();

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

    /**
     * Returns true if the prefix contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return !argumentMultimap.getValue(prefix).isEmpty();
    }

}
