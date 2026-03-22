package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterTagCommand;
import seedu.address.model.tag.Tag;

public class FilterTagCommandParserTest {
    private FilterTagCommandParser parser = new FilterTagCommandParser();

    @Test
    public void parse_validArgs_returnsFilterTagCommand() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));

        assertParseSuccess(parser, "/ friend", new FilterTagCommand(tags));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser,
                "1, 2, 3 / test",
                "Error: Format is invalid.\n" + FilterTagCommand.MESSAGE_FORMAT);
    }
}
