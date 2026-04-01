package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.MeetingDate;

/**
 * Parses input arguments and creates a new AddMeetingCommand object.
 */
public class AddMeetingCommandParser implements Parser<AddMeetingCommand> {

    @Override
    public AddMeetingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE);

        boolean isIndexMissing = argMultimap.getPreamble().isEmpty();
        boolean isDescriptionMissing = !isPrefixPresent(argMultimap, PREFIX_MEETING_DESCRIPTION);
        boolean isDateMissing = !isPrefixPresent(argMultimap, PREFIX_MEETING_DATE);

        if (isDescriptionMissing || isDateMissing) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE);

        // Parse indices if index is not missing, else give empty set
        Set<Index> indices = new HashSet<>();
        if (!isIndexMissing) {
            indices = ParserUtil.parseIndices(argMultimap.getPreamble(), AddMeetingCommand.MESSAGE_USAGE);
        }

        // Parse description
        Description parsedDescription = ParserUtil.parseDescription(
                argMultimap.getValue(PREFIX_MEETING_DESCRIPTION).get());

        // Parse date
        MeetingDate parsedDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_MEETING_DATE).get());

        return new AddMeetingCommand(indices, parsedDescription, parsedDate);
    }

    /**
     * Returns true if the prefix contains a value in the given ArgumentMultimap.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }
}
