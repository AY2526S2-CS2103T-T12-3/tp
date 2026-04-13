package seedu.address.logic.parser;

import static seedu.address.logic.Messages.CONTACT_TYPE;
import static seedu.address.logic.Messages.MESSAGE_BLANK_FIND_FIELD_INPUT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDICES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        List<String> descriptionKeywords = argMultimap.getAllValues(PREFIX_MEETING_DESCRIPTION);
        List<String> dateKeywords = argMultimap.getAllValues(PREFIX_MEETING_DATE);
        List<String> personIndicesList = argMultimap.getAllValues(PREFIX_CONTACT_INDICES);

        validateFields(preamble, descriptionKeywords, dateKeywords, personIndicesList);

        List<Set<Index>> personIndexGroups = getPersonIndexGroups(personIndicesList);

        return new FindMeetingCommand(descriptionKeywords, dateKeywords, personIndexGroups);
    }

    private static void validateFields(String preamble,
            List<String> descriptionKeywords,
            List<String> dateKeywords,
            List<String> personIndicesList) throws ParseException {

        if (!preamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindMeetingCommand.MESSAGE_USAGE));
        }

        if (descriptionKeywords.isEmpty() && dateKeywords.isEmpty() && personIndicesList.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            FindMeetingCommand.MESSAGE_NO_PARAMS_FOUND));
        }

        validateFieldInput(descriptionKeywords);
        validateFieldInput(dateKeywords);
        validateFieldInput(personIndicesList);
    }

    /**
     * Validates that a supplied prefixed field contains only non-blank values.
     *
     * @param keywords  Keywords parsed from user input.
     * @throws ParseException If the prefix is not empty but its values are blank.
     */
    private static void validateFieldInput(List<String> keywords) throws ParseException {
        if (containsBlankValues(keywords)) {
            throw new ParseException(
                    String.format(MESSAGE_BLANK_FIND_FIELD_INPUT, FindMeetingCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if any value in the list is blank.
     */
    private static boolean containsBlankValues(List<String> values) {
        return values.stream().anyMatch(String::isBlank);
    }

    /**
     * Parses the list of raw index strings into groups of {@code Index}.
     * Each element in {@code personIndicesList} corresponds to one {@code i/} prefix.
     * Within each element, comma-separated values are treated as a single group (AND).
     * Multiple groups represent OR conditions.
     *
     * @param personIndicesList List of raw index strings from input
     * @return List of index groups
     * @throws ParseException if any index is invalid or does not conform to expected format
     */
    private static List<Set<Index>> getPersonIndexGroups(List<String> personIndicesList)
            throws ParseException {
        List<Set<Index>> personIndexGroups = new ArrayList<>();

        for (String indicesGroup : personIndicesList) {
            if (!indicesGroup.isEmpty()) {
                Set<Index> parsedGroup = ParserUtil.parseIndices(indicesGroup, CONTACT_TYPE,
                        String.format(MESSAGE_INVALID_INDEX, CONTACT_TYPE));
                personIndexGroups.add(parsedGroup);
            }
        }

        return personIndexGroups;
    }
}
