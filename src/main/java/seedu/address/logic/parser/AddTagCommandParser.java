package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG_SEPARATOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMA;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class AddTagCommandParser implements Parser<AddTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ADD_TAG_SEPARATOR);

        Set<Index> indices = parseIndicesForAddTag(argMultimap.getPreamble());

        Set<Tag> tags = new HashSet<>();
        parseTagsForAddTag(argMultimap.getAllValues(PREFIX_ADD_TAG_SEPARATOR)).ifPresent(tags::addAll);

        if (tags.isEmpty()) {
            throw new ParseException(AddTagCommand.MESSAGE_NO_TAGS);
        }

        return new AddTagCommand(indices, tags);
    }

    private Set<Index> parseIndicesForAddTag(String indicesString) throws ParseException {
        String[] indices = indicesString.split(PREFIX_COMMA.toString());

        Set<Index> indexSet = new HashSet<>();
        try {
            for (String index : indices) {
                indexSet.add(ParserUtil.parseIndex(index));
            }
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE), pe);
        }

        return indexSet;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForAddTag(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}
