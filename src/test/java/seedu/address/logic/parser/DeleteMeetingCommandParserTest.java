package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SINGLE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDICES_MULTIPLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDEX_SINGLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDICES_MULTIPLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_NEGATIVE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_NON_NUMERIC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteMeetingCommand;

/**
 * Tests for DeleteMeetingCommandParser using white-box testing.
 * Only parses valid indices and invalid formats; detailed index parsing is tested in ParserUtilTest.
 */
public class DeleteMeetingCommandParserTest {

    private final DeleteMeetingCommandParser parser = new DeleteMeetingCommandParser();

    @Test
    public void parse_validSingleIndex_returnsDeleteMeetingCommand() {
        assertParseSuccess(parser, INPUT_INDEX_SINGLE,
                new DeleteMeetingCommand(VALID_INDEX_SINGLE));
    }

    @Test
    public void parse_validMultipleIndices_returnsDeleteMeetingCommand() {
        assertParseSuccess(parser, INPUT_INDICES_MULTIPLE,
                new DeleteMeetingCommand(VALID_INDICES_MULTIPLE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_outOfBoundArgs_throwsParseException() {
        assertParseFailure(parser, INVALID_INPUT_INDEX_NEGATIVE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, INVALID_INPUT_INDEX_NON_NUMERIC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }
}
