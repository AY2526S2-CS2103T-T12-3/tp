package seedu.address.logic.parser;

import static seedu.address.logic.Messages.CONTACT_TYPE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEPARATOR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
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
    private static final String INVALID_TAG = "/hubby"; // '*' not allowed in tags

    private static final String VALID_TAG_INPUT_ONE = " " + PREFIX_SEPARATOR + VALID_TAG_ONE;
    private static final String VALID_TAG_INPUT_TWO = " " + PREFIX_SEPARATOR + VALID_TAG_TWO;
    private static final String EMPTY_TAG_INPUT = " " + PREFIX_SEPARATOR + EMPTY_TAG;
    private static final String INVALID_TAG_INPUT = " " + PREFIX_SEPARATOR + INVALID_TAG;


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE);

    private final AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        // only add tag separator written
        assertParseFailure(parser, " " + PREFIX_SEPARATOR, MESSAGE_INVALID_FORMAT);

        // only space and tag separator written
        assertParseFailure(parser, EMPTY_TAG_INPUT, MESSAGE_INVALID_FORMAT);

        // no index specified
        assertParseFailure(parser, VALID_TAG_INPUT_ONE, MESSAGE_INVALID_FORMAT);

        // no tags specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + VALID_TAG_INPUT_ONE,
                String.format(MESSAGE_INVALID_INDEX, CONTACT_TYPE));

        // zero index
        assertParseFailure(parser, "0" + VALID_TAG_INPUT_ONE,
                String.format(MESSAGE_INVALID_INDEX, CONTACT_TYPE));

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string" + VALID_TAG_INPUT_ONE,
                String.format(MESSAGE_INVALID_INDEX, CONTACT_TYPE));
    }

    @Test
    public void parse_invalidTags_failure() {
        assertParseFailure(parser, "1" + INVALID_TAG_INPUT, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        assertParseFailure(parser, "1" + EMPTY_TAG_INPUT, AddTagCommand.MESSAGE_NO_TAGS); // empty tag

        // valid tag + empty tag
        assertParseFailure(parser, "1" + VALID_TAG_INPUT_ONE + EMPTY_TAG_INPUT, Tag.MESSAGE_CONSTRAINTS);

        // empty tag + valid tag
        assertParseFailure(parser, "1" + EMPTY_TAG_INPUT + VALID_TAG_INPUT_ONE, Tag.MESSAGE_CONSTRAINTS);

        // valid tag + empty tag + valid tag
        assertParseFailure(parser, "1"
                + VALID_TAG_INPUT_ONE + EMPTY_TAG_INPUT + VALID_TAG_INPUT_ONE, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_oneIndexOneTag_success() {
        Index targetIndex = INDEX_SECOND_PERSON;

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_ONE));
        AddTagCommand expectedCommand = new AddTagCommand(targetIndex, tags);

        String userInput = targetIndex.getOneBased() + VALID_TAG_INPUT_ONE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneIndexMultipleTags_success() {
        Index targetIndex = INDEX_SECOND_PERSON;

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_ONE));
        tags.add(new Tag(VALID_TAG_TWO));
        AddTagCommand expectedCommand = new AddTagCommand(targetIndex, tags);

        String userInput = targetIndex.getOneBased() + VALID_TAG_INPUT_ONE + VALID_TAG_INPUT_TWO;
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
                + VALID_TAG_INPUT_ONE + VALID_TAG_INPUT_TWO;
        System.out.println(userInput);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
