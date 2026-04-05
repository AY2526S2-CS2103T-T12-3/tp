package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_BLANK_FIND_FIELD_INPUT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 */
public class FindCommandParser implements Parser<FindCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String preamble = argMultimap.getPreamble().trim();

        List<String> nameKeywords = argMultimap.getAllValues(PREFIX_NAME);
        List<String> phoneKeywords = argMultimap.getAllValues(PREFIX_PHONE);
        List<String> emailKeywords = argMultimap.getAllValues(PREFIX_EMAIL);

        boolean hasNamePrefix = args.contains(PREFIX_NAME.getPrefix());
        boolean hasPhonePrefix = args.contains(PREFIX_PHONE.getPrefix());
        boolean hasEmailPrefix = args.contains(PREFIX_EMAIL.getPrefix());

        boolean hasAnyPrefixedSearch = hasNamePrefix || hasPhonePrefix || hasEmailPrefix;

        // Prevent mixing global + prefix search
        if (!preamble.isEmpty() && hasAnyPrefixedSearch) {
            throw new ParseException("Cannot mix global search with prefixed search.");
        }

        // Validate that prefixed fields are not blank
        if (hasNamePrefix && containsOnlyBlankValues(nameKeywords)) {
            throw new ParseException(
                    String.format(MESSAGE_BLANK_FIND_FIELD_INPUT, FindCommand.MESSAGE_USAGE));
        }

        if (hasPhonePrefix && containsOnlyBlankValues(phoneKeywords)) {
            throw new ParseException(
                    String.format(MESSAGE_BLANK_FIND_FIELD_INPUT, FindCommand.MESSAGE_USAGE));
        }

        if (hasEmailPrefix && containsOnlyBlankValues(emailKeywords)) {
            throw new ParseException(
                    String.format(MESSAGE_BLANK_FIND_FIELD_INPUT, FindCommand.MESSAGE_USAGE));
        }

        List<String> globalKeywords = preamble.isEmpty()
                ? List.of()
                : Arrays.asList(preamble.split("\\s+"));

        return new FindCommand(new PersonMatchesKeywordsPredicate(
                globalKeywords,
                nameKeywords,
                phoneKeywords,
                emailKeywords));
    }

    /**
     * Returns true if all values are blank after trimming.
     */
    private boolean containsOnlyBlankValues(List<String> values) {
        return values.isEmpty() || values.stream().allMatch(value -> value.trim().isEmpty());
    }
}
