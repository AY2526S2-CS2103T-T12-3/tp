package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_BLANK_FIND_FIELD_INPUT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

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
     * FindCommand and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected
     *                        format.
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        String preamble = normalizeWhitespace(argMultimap.getPreamble());

        List<String> nameKeywords = argMultimap.getAllValues(PREFIX_NAME);
        List<String> phoneKeywords = argMultimap.getAllValues(PREFIX_PHONE);
        List<String> emailKeywords = argMultimap.getAllValues(PREFIX_EMAIL);

        validatePrefixedSearch(args, preamble, nameKeywords, phoneKeywords, emailKeywords);

        return new FindCommand(createPredicate(preamble, nameKeywords, phoneKeywords, emailKeywords));
    }

    /**
     * Validates prefixed search input:
     * - global and prefixed search cannot be mixed
     * - prefixed fields must not be blank
     */
    private void validatePrefixedSearch(String args, String preamble,
            List<String> nameKeywords,
            List<String> phoneKeywords,
            List<String> emailKeywords) throws ParseException {
        boolean hasNamePrefix = args.contains(PREFIX_NAME.getPrefix());
        boolean hasPhonePrefix = args.contains(PREFIX_PHONE.getPrefix());
        boolean hasEmailPrefix = args.contains(PREFIX_EMAIL.getPrefix());

        boolean hasAnyPrefixedSearch = hasNamePrefix || hasPhonePrefix || hasEmailPrefix;

        if (!preamble.isEmpty() && hasAnyPrefixedSearch) {
            throw new ParseException("Cannot mix global search with prefixed search.");
        }

        validateFieldInput(hasNamePrefix, nameKeywords);
        validateFieldInput(hasPhonePrefix, phoneKeywords);
        validateFieldInput(hasEmailPrefix, emailKeywords);
    }

    /**
     * Validates that a prefixed field is not blank.
     */
    private void validateFieldInput(boolean hasPrefix, List<String> keywords) throws ParseException {
        if (hasPrefix && containsOnlyBlankValues(keywords)) {
            throw new ParseException(
                    String.format(MESSAGE_BLANK_FIND_FIELD_INPUT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Creates the predicate for the find command.
     */
    private PersonMatchesKeywordsPredicate createPredicate(String preamble,
            List<String> nameKeywords,
            List<String> phoneKeywords,
            List<String> emailKeywords) {
        List<String> globalKeywords = preamble.isEmpty()
                ? List.of()
                : List.of(preamble);

        return new PersonMatchesKeywordsPredicate(
                globalKeywords,
                nameKeywords,
                phoneKeywords,
                emailKeywords);
    }

    /**
     * Normalizes internal whitespace in a string.
     */
    private String normalizeWhitespace(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if all values are blank after trimming.
     */
    private boolean containsOnlyBlankValues(List<String> values) {
        return values.isEmpty() || values.stream().allMatch(value -> value.trim().isEmpty());
    }
}
