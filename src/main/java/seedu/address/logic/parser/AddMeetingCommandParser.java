package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;

import java.time.LocalDate;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddMeetingCommand object.
 */
public class AddMeetingCommandParser implements Parser<AddMeetingCommand> {

    @Override
    public AddMeetingCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_MEETING_INDEX,
                        PREFIX_MEETING_DESCRIPTION,
                        PREFIX_MEETING_DATE);

        boolean isIndexMissing = !isPrefixPresent(argMultimap, PREFIX_MEETING_INDEX);
        boolean isDescriptionMissing = !isPrefixPresent(argMultimap, PREFIX_MEETING_DESCRIPTION);
        boolean isDateMissing = !isPrefixPresent(argMultimap, PREFIX_MEETING_DATE);
        boolean isPreamblePresent = !argMultimap.getPreamble().isEmpty();

        if (isIndexMissing || isDescriptionMissing || isDateMissing || isPreamblePresent) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    AddMeetingCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_MEETING_INDEX,
                PREFIX_MEETING_DESCRIPTION,
                PREFIX_MEETING_DATE);

        // Parse indices
        Set<Index> indices = ParserUtil.parseIndices(
                argMultimap.getValue(PREFIX_MEETING_INDEX).get(),
                AddMeetingCommand.MESSAGE_USAGE);

        // Parse description
        String description = argMultimap.getValue(PREFIX_MEETING_DESCRIPTION).get().trim();

        // Parse date
        String date = argMultimap.getValue(PREFIX_MEETING_DATE).get().trim();
        LocalDate parsedDate = ParserUtil.parseDate(date);

        return new AddMeetingCommand(indices, description, parsedDate);
    }

    /**
     * Returns true if the prefix contains a value in the given ArgumentMultimap.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }
}
