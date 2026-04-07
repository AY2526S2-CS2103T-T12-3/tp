package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_CONTACT_TO_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditMeetingCommand;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditMeetingCommand object.
 */
public class EditMeetingCommandParser implements Parser<EditMeetingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditMeetingCommand
     * and returns an EditMeetingCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    @Override
    public EditMeetingCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE,
                PREFIX_ADD_CONTACT_TO_MEETING_INDEX, PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX);

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE,
                PREFIX_ADD_CONTACT_TO_MEETING_INDEX, PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX);

        Index meetingIndex;
        try {
            meetingIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditMeetingCommand.MESSAGE_USAGE));
        }

        EditMeetingDescriptor descriptor = setEditMeetingDescriptor(argMultimap);

        return new EditMeetingCommand(meetingIndex, descriptor);
    }

    /**
     * Parses the provided {@code ArgumentMultimap} and returns an {@code EditPersonDescriptor}
     * containing the fields to edit.
     *
     * @param argMultimap The {@code ArgumentMultimap} containing user input arguments.
     * @return An {@code EditMeetingDescriptor} populated with parsed values.
     * @throws ParseException If no fields are provided for editing.
     */
    private EditMeetingDescriptor setEditMeetingDescriptor(ArgumentMultimap argMultimap) throws ParseException {
        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();

        if (argMultimap.getValue(PREFIX_MEETING_DESCRIPTION).isPresent()) {
            descriptor.setDescription(ParserUtil.parseDescription(
                    argMultimap.getValue(PREFIX_MEETING_DESCRIPTION).get()));
        }

        if (argMultimap.getValue(PREFIX_MEETING_DATE).isPresent()) {
            descriptor.setDate(ParserUtil.parseDate(
                    argMultimap.getValue(PREFIX_MEETING_DATE).get()));
        }

        if (argMultimap.getValue(PREFIX_ADD_CONTACT_TO_MEETING_INDEX).isPresent()) {
            descriptor.setPersonIndicesToAdd(ParserUtil.parseIndices(
                    argMultimap.getValue(PREFIX_ADD_CONTACT_TO_MEETING_INDEX).get(), EditMeetingCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX).isPresent()) {
            descriptor.setPersonIndicesToDelete(ParserUtil.parseIndices(
                    argMultimap.getValue(PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX).get(),
                    EditMeetingCommand.MESSAGE_USAGE));
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditMeetingCommand.MESSAGE_NOT_EDITED);
        }

        return descriptor;
    }
}
