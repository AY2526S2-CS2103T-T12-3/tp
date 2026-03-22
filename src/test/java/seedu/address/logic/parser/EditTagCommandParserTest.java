package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

public class EditTagCommandParserTest {
    private EditTagCommandParser parser = new EditTagCommandParser();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void parse_validArgs_returnsEditTagCommand() {
        List<Index> targetIndices = new ArrayList<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        Tag oldTag = new Tag("friend");
        Tag newTag = new Tag("closefriend");

        assertParseSuccess(parser, "1 o/ friend n/ closefriend",
                new EditTagCommand(targetIndices, oldTag, newTag));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String userInput = " o/ n/ friend";

        assertParseFailure(parser,
                userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_FORMAT));
    }
}
