package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_20260325;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_20260401;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TEAM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INDEX_SINGLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INDICES_MULTIPLE;
import static seedu.address.logic.parser.ParserUtilTest.INPUT_DATE_20260325;
import static seedu.address.logic.parser.ParserUtilTest.INPUT_DATE_20260401;
import static seedu.address.logic.parser.ParserUtilTest.INPUT_DESC_PROJECT;
import static seedu.address.logic.parser.ParserUtilTest.INPUT_DESC_TEAM;
import static seedu.address.logic.parser.ParserUtilTest.INPUT_INDEX_SINGLE;
import static seedu.address.logic.parser.ParserUtilTest.INPUT_INDICES_MULTIPLE;
import static seedu.address.logic.parser.ParserUtilTest.INVALID_INPUT_DATE;
import static seedu.address.logic.parser.ParserUtilTest.INVALID_INPUT_DESCRIPTION;
import static seedu.address.logic.parser.ParserUtilTest.INVALID_INPUT_INDEX_NEGATIVE;
import static seedu.address.logic.parser.ParserUtilTest.INVALID_INPUT_INDEX_NON_NUMERIC;
import static seedu.address.logic.parser.ParserUtilTest.INVALID_INPUT_INDEX_ZERO;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddMeetingCommand;

public class AddMeetingCommandParserTest {

    private AddMeetingCommandParser parser = new AddMeetingCommandParser();

    @Test
    public void parse_allFieldsPresent_singleIndex_success() {
        // single index, valid description and date
        assertParseSuccess(parser, INPUT_INDEX_SINGLE + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                new AddMeetingCommand(VALID_INDEX_SINGLE, VALID_DESCRIPTION_PROJECT, VALID_DATE_20260325));
    }

    @Test
    public void parse_allFieldsPresent_multipleIndices_success() {
        AddMeetingCommand expectedCommand = new AddMeetingCommand(VALID_INDICES_MULTIPLE, VALID_DESCRIPTION_TEAM, VALID_DATE_20260401);
        assertParseSuccess(parser, INPUT_INDICES_MULTIPLE + INPUT_DESC_TEAM + INPUT_DATE_20260401, expectedCommand);
    }

    @Test
    public void parse_missingFields_failure() {
        // missing index
        assertParseFailure(parser, INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // missing description
        assertParseFailure(parser, VALID_INDEX_SINGLE + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // missing date
        assertParseFailure(parser, VALID_INDEX_SINGLE + INPUT_DESC_PROJECT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // all missing
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        // zero
        assertParseFailure(parser, INVALID_INPUT_INDEX_ZERO + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // negative
        assertParseFailure(parser, INVALID_INPUT_INDEX_NEGATIVE + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // non-numeric
        assertParseFailure(parser, INVALID_INPUT_INDEX_NON_NUMERIC + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDescription_failure() {
        assertParseFailure(parser, VALID_INDEX_SINGLE + INVALID_INPUT_DESCRIPTION + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, VALID_INDEX_SINGLE + INPUT_DESC_PROJECT + INVALID_INPUT_DATE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_failure() {
        // extra preamble before any prefixes
        assertParseFailure(parser, "randomPreamble " + VALID_INDEX_SINGLE + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        // duplicated index
        assertParseFailure(parser, VALID_INDEX_SINGLE + " " + PREFIX_MEETING_INDEX + VALID_INDEX_SINGLE
                        + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // duplicated description
        assertParseFailure(parser, VALID_INDEX_SINGLE + INPUT_DESC_PROJECT + INPUT_DESC_TEAM + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // duplicated date
        assertParseFailure(parser, VALID_INDEX_SINGLE + INPUT_DESC_PROJECT + INPUT_DATE_20260325 + INPUT_DATE_20260401,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }
}
