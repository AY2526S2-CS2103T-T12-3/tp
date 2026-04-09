package seedu.address.logic.parser;

import static seedu.address.logic.Messages.CONTACT_TYPE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SET_SINGLE;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDICES_SET_MULTIPLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;

public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(VALID_INDEX_SET_SINGLE));
    }

    @Test
    public void parse_validMultipleIndices_returnsDeleteCommand() {
        assertParseSuccess(parser, "1,2,3", new DeleteCommand(VALID_INDICES_SET_MULTIPLE));
    }


    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_INDEX, CONTACT_TYPE));
    }
}
