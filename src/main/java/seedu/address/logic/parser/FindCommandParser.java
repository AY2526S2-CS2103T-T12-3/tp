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
        validateNotBlank(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        String preamble = argMultimap.getPreamble().trim();

        List<String> nameKeywords = argMultimap.getAllValues(PREFIX_NAME);
        List<String> phoneKeywords = argMultimap.getAllValues(PREFIX_PHONE);
        List<String> emailKeywords = argMultimap.getAllValues(PREFIX_EMAIL);

        validatePrefixedSearch(preamble, nameKeywords, phoneKeywords, emailKeywords);

        return new FindCommand(createPredicate(preamble, nameKeywords, phoneKeywords, emailKeywords));
    }

    private void validateNotBlank(String args) throws ParseException {
        if (args.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
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

        validateFieldInput(nameKeywords);
        validateFieldInput(phoneKeywords);
        validateFieldInput(emailKeywords);
    }

    /**
     * Validates that a supplied prefixed field contains only non-blank values.
     *
     * @param keywords  Keywords parsed from user input.
     * @throws ParseException If the prefix was supplied but its values are blank.
     */
    private void validateFieldInput(List<String> keywords) throws ParseException {
        if (containsBlankValues(keywords)) {
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
        List<String> globalKeywords = preamble.isBlank()
                ? List.of()
                : List.of(preamble);

        return new PersonMatchesKeywordsPredicate(
                globalKeywords,
                nameKeywords,
                phoneKeywords,
                emailKeywords);
    }

    /**
     * Returns true if any value in the list is blank.
     */
    private boolean containsBlankValues(List<String> values) {
        return values.stream().anyMatch(String::isBlank);
    }
}
