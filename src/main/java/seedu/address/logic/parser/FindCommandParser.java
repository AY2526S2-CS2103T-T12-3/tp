package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_BLANK_FIND_FIELD_INPUT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;

import seedu.address.logic.Messages;
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
     * @throws ParseException If the user input does not conform the expected
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

        validatePrefixedSearch(preamble, nameKeywords, phoneKeywords, emailKeywords);

        return new FindCommand(createPredicate(preamble, nameKeywords, phoneKeywords, emailKeywords));
    }

    /**
     * Validates prefixed search input.
     * Prevents mixing global search and prefixed search.
     * Ensures that any prefix present is not followed only by blank values.
     *
     * @param preamble      Global keywords.
     * @param nameKeywords  Name keywords.
     * @param phoneKeywords Phone keywords.
     * @param emailKeywords Email keywords.
     * @throws ParseException If the input mixes global and prefixed search,
     *                        or if a supplied prefixed field is blank.
     */
    private void validatePrefixedSearch(String preamble,
            List<String> nameKeywords,
            List<String> phoneKeywords,
            List<String> emailKeywords) throws ParseException {
        boolean hasNamePrefix = !nameKeywords.isEmpty();
        boolean hasPhonePrefix = !phoneKeywords.isEmpty();
        boolean hasEmailPrefix = !emailKeywords.isEmpty();

        boolean hasAnyPrefixedSearch = hasNamePrefix || hasPhonePrefix || hasEmailPrefix;

        if (!preamble.isEmpty() && hasAnyPrefixedSearch) {
            throw new ParseException(Messages.MESSAGE_MIX_GLOBAL_AND_PREFIX_SEARCH);
        }

        validateFieldInput(hasNamePrefix, nameKeywords);
        validateFieldInput(hasPhonePrefix, phoneKeywords);
        validateFieldInput(hasEmailPrefix, emailKeywords);
    }

    /**
     * Validates that a supplied prefixed field contains at least one non-blank value.
     *
     * @param hasPrefix True if have prefix, false if not.
     * @param keywords  Keywords parsed from user input.
     * @throws ParseException If the prefix was supplied but its values are blank.
     */
    private void validateFieldInput(boolean hasPrefix, List<String> keywords) throws ParseException {
        if (hasPrefix && containsOnlyBlankValues(keywords)) {
            throw new ParseException(
                    String.format(MESSAGE_BLANK_FIND_FIELD_INPUT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Creates the predicate for the find command.
     *
     * @param preamble      Global keywords.
     * @param nameKeywords  Name keywords.
     * @param phoneKeywords Phone keywords.
     * @param emailKeywords Email keywords.
     * @return A PersonMatchesKeywordsPredicate.
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
     * Trims the input and collapses consecutive whitespace into a single space.
     *
     * @param input User input for global search.
     * @return The normalized string.
     */
    private String normalizeWhitespace(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if the list is empty, or if all values are blank after trimming.
     *
     * @param values Values in prefixed search.
     * @return True if no meaningful values, false otherwise.
     */
    private boolean containsOnlyBlankValues(List<String> values) {
        return values.isEmpty() || values.stream().allMatch(value -> value.trim().isEmpty());
    }
}
