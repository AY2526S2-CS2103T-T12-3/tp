package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG_SEPARATOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.model.tag.Tag;

public class AddTagCommandParserTest {

    private static final String VALID_TAG_ONE = "One";
    private static final String VALID_TAG_TWO = "Two";
    private static final String EMPTY_TAG = "";
    private static final String INVALID_TAG = "hubby*"; // '*' not allowed in tags

    private static final String VALID_TAG_DESC_ONE = " " + PREFIX_ADD_TAG_SEPARATOR + VALID_TAG_ONE;
    private static final String VALID_TAG_DESC_TWO = " " + PREFIX_ADD_TAG_SEPARATOR + VALID_TAG_TWO;
    private static final String EMPTY_TAG_DESC = " " + PREFIX_ADD_TAG_SEPARATOR + EMPTY_TAG;
    private static final String INVALID_TAG_DESC = " " + PREFIX_ADD_TAG_SEPARATOR + INVALID_TAG;


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE);

    private final AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        // only add tag separator written
        assertParseFailure(parser, PREFIX_ADD_TAG_SEPARATOR.toString(), MESSAGE_INVALID_FORMAT);

        // only space and tag separator written
        assertParseFailure(parser, EMPTY_TAG_DESC, MESSAGE_INVALID_FORMAT);

        // no index specified
        assertParseFailure(parser, VALID_TAG_DESC_ONE, MESSAGE_INVALID_FORMAT);

        // no tags specified
        assertParseFailure(parser, "1", AddTagCommand.MESSAGE_NO_TAGS);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + VALID_TAG_DESC_ONE, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + VALID_TAG_DESC_ONE, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string" + VALID_TAG_DESC_ONE, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidTags_failure() {
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        assertParseFailure(parser, "1" + EMPTY_TAG_DESC, AddTagCommand.MESSAGE_NO_TAGS); // empty tag

        // valid tag + empty tag
        assertParseFailure(parser, "1" + VALID_TAG_DESC_ONE + EMPTY_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // empty tag + valid tag
        assertParseFailure(parser, "1" + EMPTY_TAG_DESC + VALID_TAG_DESC_ONE, Tag.MESSAGE_CONSTRAINTS);

        // valid tag + empty tag + valid tag
        assertParseFailure(parser, "1"
                + VALID_TAG_DESC_ONE + EMPTY_TAG_DESC + VALID_TAG_DESC_ONE, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_oneIndexOneTag_success() {
        Index targetIndex = INDEX_SECOND_PERSON;

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_ONE));
        AddTagCommand expectedCommand = new AddTagCommand(targetIndex, tags);

        String userInput = targetIndex.getOneBased() + VALID_TAG_DESC_ONE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneIndexMultipleTags_success() {
        Index targetIndex = INDEX_SECOND_PERSON;

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_ONE));
        tags.add(new Tag(VALID_TAG_TWO));
        AddTagCommand expectedCommand = new AddTagCommand(targetIndex, tags);

        String userInput = targetIndex.getOneBased() + VALID_TAG_DESC_ONE + VALID_TAG_DESC_TWO;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleIndicesMultipleTags_success() {
        Set<Index> indices = new HashSet<>();
        indices.add(INDEX_FIRST_PERSON);
        indices.add(INDEX_SECOND_PERSON);

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_ONE));
        tags.add(new Tag(VALID_TAG_TWO));
        AddTagCommand expectedCommand = new AddTagCommand(indices, tags);

        String userInput =
                INDEX_FIRST_PERSON.getOneBased() + PREFIX_COMMA.toString() + INDEX_SECOND_PERSON.getOneBased()
                + VALID_TAG_DESC_ONE + VALID_TAG_DESC_TWO;
        System.out.println(userInput);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
