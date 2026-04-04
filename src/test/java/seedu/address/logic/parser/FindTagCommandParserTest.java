package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.model.tag.Tag;

public class FindTagCommandParserTest {
    private FindTagCommandParser parser = new FindTagCommandParser();

    @Test
    public void parse_validArgs_returnsFindTagCommand() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));

        assertParseSuccess(parser, " / friend", new FindTagCommand(tags));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser,
                "1, 2, 3 / test",
                "Error: Format is invalid.\n" + FindTagCommand.MESSAGE_FORMAT);
    }

    @Test
    public void parse_invalidArgsSlash_throwsParseException() {
        assertParseFailure(parser,
                "/",
                FindTagCommand.MESSAGE_NO_TAGS);
    }

    @Test
    public void parse_invalidArgsNoSlash_throwsParseException() {
        assertParseFailure(parser,
                "friend",
                FindTagCommand.MESSAGE_NO_TAGS);
    }
}
