package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MEETING_TYPE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SET_SINGLE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDICES_SET_MULTIPLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDEX_SINGLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDICES_MULTIPLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_NEGATIVE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_NON_NUMERIC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteMeetingCommand;

public class DeleteMeetingCommandParserTest {

    private final DeleteMeetingCommandParser parser = new DeleteMeetingCommandParser();

    @Test
    public void parse_validSingleIndex_returnsDeleteMeetingCommand() {
        assertParseSuccess(parser, INPUT_INDEX_SINGLE,
                new DeleteMeetingCommand(VALID_INDEX_SET_SINGLE));
    }

    @Test
    public void parse_validMultipleIndices_returnsDeleteMeetingCommand() {
        assertParseSuccess(parser, INPUT_INDICES_MULTIPLE,
                new DeleteMeetingCommand(VALID_INDICES_SET_MULTIPLE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_outOfBoundArgs_throwsParseException() {
        assertParseFailure(parser, INVALID_INPUT_INDEX_NEGATIVE,
                String.format(MESSAGE_INVALID_INDEX, MEETING_TYPE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, INVALID_INPUT_INDEX_NON_NUMERIC,
                String.format(MESSAGE_INVALID_INDEX, MEETING_TYPE));
    }
}
