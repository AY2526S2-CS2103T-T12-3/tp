package seedu.address.logic.parser;

import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDEX_SINGLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_NEGATIVE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_NON_NUMERIC;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INVALID_INPUT_INDEX_ZERO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_PERSON_TO_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_PERSON_FROM_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalMeetings.PROJECT_MEETING;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditMeetingCommand;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

class EditMeetingCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    EditMeetingCommand.MESSAGE_USAGE);

    private EditMeetingCommandParser parser = new EditMeetingCommandParser();

    @Test
    void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, PREFIX_MEETING_DESCRIPTION
                + PROJECT_MEETING.getDescription().toString(),
                MESSAGE_INVALID_FORMAT);

        // no fields specified
        assertParseFailure(parser, INPUT_INDEX_SINGLE, EditMeetingCommand.MESSAGE_NOT_EDITED);

        // no index and no fields
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, INVALID_INPUT_INDEX_NEGATIVE + PREFIX_MEETING_DESCRIPTION
                + PROJECT_MEETING.getDescription(), MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, INVALID_INPUT_INDEX_ZERO + PREFIX_MEETING_DESCRIPTION
                + PROJECT_MEETING.getDescription(), MESSAGE_INVALID_FORMAT);

        // non-integer index
        assertParseFailure(parser, INVALID_INPUT_INDEX_NON_NUMERIC + PREFIX_MEETING_DESCRIPTION
                + PROJECT_MEETING.getDescription(), MESSAGE_INVALID_FORMAT);
    }

    @Test
    void parse_allFieldsSpecified_success() throws ParseException {
        String userInput = INDEX_SECOND_PERSON.getOneBased()
                + " " + PREFIX_MEETING_DESCRIPTION + PROJECT_MEETING.getDescription()
                + " " + PREFIX_MEETING_DATE + PROJECT_MEETING.getDate()
                + " " + PREFIX_ADD_PERSON_TO_MEETING_INDEX + "3"
                + " " + PREFIX_DELETE_PERSON_FROM_MEETING_INDEX + "1";

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(PROJECT_MEETING.getDescription());
        descriptor.setDate(PROJECT_MEETING.getDate());
        descriptor.setPersonIndicesToAdd(Set.of(INDEX_THIRD_PERSON));
        descriptor.setPersonIndicesToDelete(Set.of(INDEX_FIRST_PERSON));

        EditMeetingCommand expectedCommand = new EditMeetingCommand(INDEX_SECOND_PERSON, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_someFieldsSpecified_success() throws ParseException {
        // Only description and date
        String userInput = INPUT_INDEX_SINGLE
                + " " + PREFIX_MEETING_DESCRIPTION + PROJECT_MEETING.getDescription()
                + " " + PREFIX_MEETING_DATE + PROJECT_MEETING.getDate();

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();
        descriptor.setDescription(PROJECT_MEETING.getDescription());
        descriptor.setDate(PROJECT_MEETING.getDate());

        EditMeetingCommand expectedCommand = new EditMeetingCommand(INDEX_FIRST_PERSON, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_duplicatePrefixes_failure() {
        String userInput = INPUT_INDEX_SINGLE
                + " " + PREFIX_MEETING_DESCRIPTION + PROJECT_MEETING.getDescription()
                + " " + PREFIX_MEETING_DESCRIPTION + PROJECT_MEETING.getDescription();

        assertParseFailure(parser, userInput,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_DESCRIPTION));
    }

    @Test
    void parse_noFieldsEdited_failure() {
        assertParseFailure(parser, INPUT_INDEX_SINGLE,
                EditMeetingCommand.MESSAGE_NOT_EDITED);
    }
}
