package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_PERSON_TO_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_PERSON_FROM_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.ParserUtil.parseIndices;

import java.util.Set;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditMeetingCommand;
import seedu.address.logic.commands.EditMeetingCommand.EditMeetingDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

public class EditMeetingCommandParser implements Parser<EditMeetingCommand> {

    @Override
    public EditMeetingCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE,
                PREFIX_ADD_PERSON_TO_MEETING_INDEX, PREFIX_DELETE_PERSON_FROM_MEETING_INDEX);

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_MEETING_DESCRIPTION, PREFIX_MEETING_DATE,
                PREFIX_ADD_PERSON_TO_MEETING_INDEX, PREFIX_DELETE_PERSON_FROM_MEETING_INDEX);

        EditMeetingDescriptor descriptor = new EditMeetingDescriptor();

        Set<Index> meetingIndices = parseIndices(argMultimap.getPreamble(),
                EditMeetingCommand.MESSAGE_USAGE);

        if (argMultimap.getValue(PREFIX_MEETING_DESCRIPTION).isPresent()) {
            String trimmedDescription = argMultimap.getValue(PREFIX_MEETING_DESCRIPTION).get().trim();
            if (!trimmedDescription.isEmpty()) {
                descriptor.setDescription(trimmedDescription);
            }
        }

        if (argMultimap.getValue(PREFIX_MEETING_DATE).isPresent()) {
                descriptor.setDate(
                        ParserUtil.parseDate(argMultimap.getValue(PREFIX_MEETING_DATE).get()));
        }

        if (argMultimap.getValue(PREFIX_ADD_PERSON_TO_MEETING_INDEX).isPresent()) {
            descriptor.setPeopleIndicesToAdd(ParserUtil.parseIndices(
                    argMultimap.getValue(PREFIX_ADD_PERSON_TO_MEETING_INDEX).get(),
                    EditMeetingCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_DELETE_PERSON_FROM_MEETING_INDEX).isPresent()) {
            descriptor.setPeopleIndicesToDelete(ParserUtil.parseIndices(
                    argMultimap.getValue(PREFIX_DELETE_PERSON_FROM_MEETING_INDEX).get(),
                    EditMeetingCommand.MESSAGE_USAGE));
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditMeetingCommand.MESSAGE_NOT_EDITED);
        }

        return new EditMeetingCommand(meetingIndices, descriptor);
    }
}
