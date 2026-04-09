package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDICES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindMeetingCommand;

public class FindMeetingCommandParserTest {

    public static final String NON_EMPTY_PREAMBLE = " hi d/ Casual icebreaker";

    private FindMeetingCommandParser parser = new FindMeetingCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindMeetingCommand.MESSAGE_NO_PARAMS_FOUND)));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, NON_EMPTY_PREAMBLE,
                String.format(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindMeetingCommand.MESSAGE_USAGE)));
    }

    @Test
    public void parse_prefixPresentButEmptyArg_throwsParseException() {
        String userInput = " " + PREFIX_MEETING_DESCRIPTION
                + " " + PREFIX_MEETING_DATE
                + " " + PREFIX_CONTACT_INDICES;
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindMeetingCommand.MESSAGE_NO_PARAMS_FOUND));
    }

    @Test
    public void parse_descriptionPrefix_returnsFindMeetingCommand() {
        FindMeetingCommand expected = new FindMeetingCommand(
                List.of(VALID_DESCRIPTION_PROJECT),
                Collections.emptyList(),
                Collections.emptySet());

        String userInput1 = " " + PREFIX_MEETING_DESCRIPTION + VALID_DESCRIPTION_PROJECT;
        String userInput2 = " " + PREFIX_MEETING_DESCRIPTION + " " + VALID_DESCRIPTION_PROJECT;

        assertParseSuccess(parser, userInput1, expected);
        assertParseSuccess(parser, userInput2, expected);
    }

    @Test
    public void parse_datePrefix_returnsFindMeetingCommand() {
        FindMeetingCommand expected = new FindMeetingCommand(
                Collections.emptyList(),
                List.of(VALID_DATE_20270325.toString()),
                Collections.emptySet());

        String userInput1 = " " + PREFIX_MEETING_DATE + VALID_DATE_20270325;
        String userInput2 = " " + PREFIX_MEETING_DATE + " " + VALID_DATE_20270325;

        assertParseSuccess(parser, userInput1, expected);
        assertParseSuccess(parser, userInput2, expected);
    }

    @Test
    public void parse_indicesPrefix_returnsFindMeetingCommand() {
        FindMeetingCommand expected = new FindMeetingCommand(
                Collections.emptyList(),
                Collections.emptyList(),
                Set.of(Index.fromOneBased(1)));

        String userInput1 = " " + PREFIX_CONTACT_INDICES + "1";
        String userInput2 = " " + PREFIX_CONTACT_INDICES + " " + "1";

        assertParseSuccess(parser, userInput1, expected);
        assertParseSuccess(parser, userInput2, expected);
    }

    @Test
    public void parse_multiplePrefixes_returnsFindMeetingCommand() {
        FindMeetingCommand expected = new FindMeetingCommand(
                List.of(VALID_DESCRIPTION_PROJECT),
                List.of(VALID_DATE_20270325.toString()),
                Collections.emptySet());

        String userInput1 = " " + PREFIX_MEETING_DESCRIPTION + VALID_DESCRIPTION_PROJECT
                + " " + PREFIX_MEETING_DATE + VALID_DATE_20270325;
        String userInput2 = " " + PREFIX_MEETING_DESCRIPTION + " " + VALID_DESCRIPTION_PROJECT
                + " " + PREFIX_MEETING_DATE + VALID_DATE_20270325;
        String userInput3 = " " + PREFIX_MEETING_DESCRIPTION + VALID_DESCRIPTION_PROJECT
                + " " + PREFIX_MEETING_DATE + " " + VALID_DATE_20270325;
        String userInput4 = " " + PREFIX_MEETING_DESCRIPTION + " " + VALID_DESCRIPTION_PROJECT
                + " " + PREFIX_MEETING_DATE + " " + VALID_DATE_20270325;

        assertParseSuccess(parser, userInput1, expected);
        assertParseSuccess(parser, userInput2, expected);
        assertParseSuccess(parser, userInput3, expected);
        assertParseSuccess(parser, userInput4, expected);
    }

}
