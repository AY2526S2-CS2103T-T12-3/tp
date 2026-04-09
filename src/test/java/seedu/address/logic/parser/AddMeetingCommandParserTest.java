package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270401;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_TEAM;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SET_SINGLE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDICES_SET_MULTIPLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddMeetingCommand;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.MeetingDate;

public class AddMeetingCommandParserTest {
    // Samples for meeting inputs
    public static final String INPUT_DESC_PROJECT = " " + PREFIX_MEETING_DESCRIPTION + VALID_DESCRIPTION_PROJECT;
    public static final String INPUT_DESC_TEAM = " " + PREFIX_MEETING_DESCRIPTION + VALID_DESCRIPTION_TEAM;

    public static final String INPUT_DATE_20260325 = " " + PREFIX_MEETING_DATE + VALID_DATE_20270325;
    public static final String INPUT_DATE_20260401 = " " + PREFIX_MEETING_DATE + VALID_DATE_20270401;

    public static final String INVALID_INPUT_DESCRIPTION = " " + PREFIX_MEETING_DESCRIPTION + "";
    public static final String INVALID_INPUT_DATE = " " + PREFIX_MEETING_DATE + "25-03-2026";

    public static final String INPUT_INDEX_SINGLE = " 1";
    public static final String INPUT_INDICES_MULTIPLE = " 1, 2, 3";
    public static final String INVALID_INPUT_INDEX_ZERO = " 0";
    public static final String INVALID_INPUT_INDEX_NEGATIVE = " -1";
    public static final String INVALID_INPUT_INDEX_NON_NUMERIC = " a,b";

    private AddMeetingCommandParser parser = new AddMeetingCommandParser();

    @Test
    public void parseAllFieldsPresent_singleIndex_success() {
        // single index, valid description and date
        assertParseSuccess(parser, INPUT_INDEX_SINGLE + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                new AddMeetingCommand(
                        VALID_INDEX_SET_SINGLE,
                        new Description(VALID_DESCRIPTION_PROJECT),
                        new MeetingDate(VALID_DATE_20270325)));
    }

    @Test
    public void parseAllFieldsPresent_multipleIndices_success() {
        AddMeetingCommand expectedCommand = new AddMeetingCommand(
                VALID_INDICES_SET_MULTIPLE,
                new Description(VALID_DESCRIPTION_TEAM),
                new MeetingDate(VALID_DATE_20270401));
        assertParseSuccess(parser, INPUT_INDICES_MULTIPLE + INPUT_DESC_TEAM + INPUT_DATE_20260401,
                expectedCommand);
    }

    @Test
    public void parse_missingIndex_success() {
        // missing index
        AddMeetingCommand expectedCommand = new AddMeetingCommand(
                new HashSet<>(),
                new Description(VALID_DESCRIPTION_TEAM),
                new MeetingDate(VALID_DATE_20270401));
        assertParseSuccess(parser, INPUT_DESC_TEAM + INPUT_DATE_20260401, expectedCommand);
    }

    @Test
    public void parse_missingFields_failure() {
        // missing description
        assertParseFailure(parser, VALID_INDEX_SET_SINGLE + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));

        // missing date
        assertParseFailure(parser, VALID_INDEX_SET_SINGLE + INPUT_DESC_PROJECT,
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
        assertParseFailure(parser, VALID_INDEX_SET_SINGLE + INVALID_INPUT_DESCRIPTION + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, VALID_INDEX_SET_SINGLE + INPUT_DESC_PROJECT + INVALID_INPUT_DATE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_failure() {
        // extra preamble before any prefixes
        assertParseFailure(parser, "randomPreamble "
                        + VALID_INDEX_SET_SINGLE + INPUT_DESC_PROJECT + INPUT_DATE_20260325,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        // duplicated description
        assertParseFailure(parser,
                VALID_INDEX_SET_SINGLE + INPUT_DESC_PROJECT + INPUT_DESC_TEAM + INPUT_DATE_20260325,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_DESCRIPTION));

        // duplicated date
        assertParseFailure(parser,
                VALID_INDEX_SET_SINGLE + INPUT_DESC_PROJECT + INPUT_DATE_20260325 + INPUT_DATE_20260401,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_DATE));
    }
}
