package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validGlobalArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand = new FindCommand(new PersonMatchesKeywordsPredicate(
                Arrays.asList("Alice", "Bob"), // global
                List.of(),
                List.of(),
                List.of()));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_namePrefix_returnsFindCommand() {
        FindCommand expected = new FindCommand(
                new PersonMatchesKeywordsPredicate(
                    List.of(),
                    List.of("Alice"),
                    List.of(),
                    List.of()));

        assertParseSuccess(parser, " n/Alice", expected);
        assertParseSuccess(parser, " n/ Alice", expected);
    }

    @Test
    public void parse_phonePrefix_returnsFindCommand() {
        FindCommand expected = new FindCommand(
                new PersonMatchesKeywordsPredicate(
                        List.of(),
                        List.of(),
                        List.of("1234"),
                        List.of()));

        assertParseSuccess(parser, " p/1234", expected);
        assertParseSuccess(parser, " p/ 1234", expected);
    }

    @Test
    public void parse_emailPrefix_returnsFindCommand() {
        FindCommand expected = new FindCommand(
                new PersonMatchesKeywordsPredicate(
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of("gmail")));

        assertParseSuccess(parser, " e/gmail", expected);
        assertParseSuccess(parser, " e/ gmail", expected);
    }

    @Test
    public void parse_multiplePrefixes_returnsFindCommand() {
        FindCommand expected = new FindCommand(
                new PersonMatchesKeywordsPredicate(
                        List.of(),
                        List.of("Alice"),
                        List.of("1234"),
                        List.of()));

        assertParseSuccess(parser, " n/Alice p/1234", expected);
        assertParseSuccess(parser, " n/ Alice p/1234", expected);
        assertParseSuccess(parser, " n/Alice p/ 1234", expected);
        assertParseSuccess(parser, " n/ Alice p/ 1234", expected);
    }

    @Test
    public void parse_mixedGlobalAndPrefix_throwsParseException() {
        assertParseFailure(parser,
                "find Alice n/Bob",
                "Cannot mix global search with prefixed search.");
    }

}
