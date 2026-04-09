package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DATE_20270325;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_DESCRIPTION_PROJECT;
import static seedu.address.logic.commands.AddMeetingCommandTest.VALID_INDEX_SET_SINGLE;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_DATE_20260325;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_DESC_PROJECT;
import static seedu.address.logic.parser.AddMeetingCommandParserTest.INPUT_INDEX_SINGLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT_INDICES;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddMeetingCommand;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteMeetingCommand;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindMeetingCommand;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListMeetingCommand;
import seedu.address.logic.commands.StarCommand;
import seedu.address.logic.commands.UnstarCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addMeeting() throws Exception {
        String commandInput = AddMeetingCommand.COMMAND_WORD + INPUT_INDEX_SINGLE
                + INPUT_DESC_PROJECT + INPUT_DATE_20260325;
        AddMeetingCommand command = (AddMeetingCommand) parser.parseCommand(commandInput);

        AddMeetingCommand expectedCommand = new AddMeetingCommand(VALID_INDEX_SET_SINGLE,
                new Description(VALID_DESCRIPTION_PROJECT),
                new MeetingDate(VALID_DATE_20270325));
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(VALID_INDEX_SET_SINGLE), command);
    }

    @Test
    public void parseCommand_deleteMeeting() throws Exception {
        String commandInput = DeleteMeetingCommand.COMMAND_WORD + INPUT_INDEX_SINGLE;
        DeleteMeetingCommand command = (DeleteMeetingCommand) parser.parseCommand(commandInput);

        DeleteMeetingCommand expectedCommand = new DeleteMeetingCommand(VALID_INDEX_SET_SINGLE);
        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_star() throws Exception {
        StarCommand command = (StarCommand) parser.parseCommand(
                StarCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        assertEquals(new StarCommand(targetIndices), command);
    }

    @Test
    public void parseCommand_unstar() throws Exception {
        UnstarCommand command = (UnstarCommand) parser.parseCommand(
                UnstarCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        assertEquals(new UnstarCommand(targetIndices), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        String globalSubstring = "foo bar baz";
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + globalSubstring);

        assertEquals(new FindCommand(
                new PersonMatchesKeywordsPredicate(
                        List.of(globalSubstring),
                        List.of(),
                        List.of(),
                        List.of())),
                command);
    }

    @Test
    public void parseCommand_findMeeting() throws Exception {
        FindMeetingCommand command = (FindMeetingCommand) parser.parseCommand(
                FindMeetingCommand.COMMAND_WORD
                + INPUT_DESC_PROJECT
                + INPUT_DATE_20260325
                + " " + PREFIX_CONTACT_INDICES + INPUT_INDEX_SINGLE);

        assertEquals(new FindMeetingCommand(
                List.of(VALID_DESCRIPTION_PROJECT),
                List.of(VALID_DATE_20270325.toString()),
                Set.of(Index.fromOneBased(1))), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listMeeting() throws Exception {
        assertTrue(parser.parseCommand(ListMeetingCommand.COMMAND_WORD) instanceof ListMeetingCommand);
        assertTrue(parser.parseCommand(ListMeetingCommand.COMMAND_WORD + " 3") instanceof ListMeetingCommand);
    }

    @Test
    public void parseCommand_addTag() throws Exception {
        AddTagCommand command = (AddTagCommand) parser.parseCommand(AddTagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " /NewTag");

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("NewTag"));
        AddTagCommand expectedCommand = new AddTagCommand(INDEX_FIRST_PERSON, tags);

        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_deleteTag() throws Exception {
        DeleteTagCommand command = (DeleteTagCommand) parser.parseCommand(DeleteTagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " /NewTag");

        Set<Tag> tags = new HashSet<>();
        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        tags.add(new Tag("NewTag"));
        DeleteTagCommand expectedCommand = new DeleteTagCommand(targetIndices, tags);

        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_editTag() throws Exception {
        EditTagCommand command = (EditTagCommand) parser.parseCommand(EditTagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " o/OldTag" + " n/NewTag");

        Set<Index> targetIndices = new HashSet<>();
        targetIndices.add(INDEX_FIRST_PERSON);
        Tag oldTag = new Tag("OldTag");
        Tag newTag = new Tag("NewTag");
        EditTagCommand expectedCommand = new EditTagCommand(targetIndices, oldTag, newTag);

        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_findTag() throws Exception {
        FindTagCommand command = (FindTagCommand) parser.parseCommand(FindTagCommand.COMMAND_WORD
                + " / NewTag");

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("NewTag"));
        FindTagCommand expectedCommand = new FindTagCommand(tags);

        assertEquals(expectedCommand, command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                        -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class,
                MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
