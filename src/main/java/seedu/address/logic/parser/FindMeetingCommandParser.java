package seedu.address.logic.parser;

import static seedu.address.logic.Messages.CONTACT_TYPE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDICES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindMeetingCommand object.
 */
public class FindMeetingCommandParser implements Parser<FindMeetingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindMeetingCommand
     * and returns a FindMeetingCommand object for execution.
     *
     * @throws ParseException If the user input does not conform the expected format.
     */
    public FindMeetingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE, PREFIX_CONTACT_INDICES);

        String preamble = argMultimap.getPreamble().trim();

        List<String> descriptionKeywords = argMultimap.getAllValues(PREFIX_MEETING_DESCRIPTION).stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        List<String> dateKeywords = argMultimap.getAllValues(PREFIX_MEETING_DATE).stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        List<String> personIndicesList = argMultimap.getAllValues(PREFIX_CONTACT_INDICES);

        Set<Index> personIndices = getPersonIndices(personIndicesList);

        if (descriptionKeywords.isEmpty() && dateKeywords.isEmpty() && personIndices.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            FindMeetingCommand.MESSAGE_NO_PARAMS_FOUND));
        }

        if (!preamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindMeetingCommand.MESSAGE_USAGE));
        }

        return new FindMeetingCommand(descriptionKeywords, dateKeywords, personIndices);
    }

    /**
     * Returns the set of person indices to be searched for.
     *
     * @param personIndicesList The list of person indices parsed from the argument string.
     * @return The set of person indices to be searched for.
     * @throws ParseException if any index is invalid or does not conform to the expected format.
     */
    private static Set<Index> getPersonIndices(List<String> personIndicesList)
            throws ParseException {
        Set<Index> personIndices = new HashSet<>();

        if (!personIndicesList.isEmpty() && !personIndicesList.get(0).isEmpty()) {
            personIndices = ParserUtil.parseIndices(personIndicesList.get(0),
                    CONTACT_TYPE, FindMeetingCommand.MESSAGE_USAGE);
        }

        return personIndices;
    }
}
