package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new DeleteTagCommand object
 */
public class DeleteTagCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteTagCommand
     * and returns a DeleteTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteTagCommand parse(String args) throws ParseException {
        /*
        Given an example command: "deletetag 1, 2, 3 / cs / backend"
        args = "1, 2, 3 / cs / backend"
        args.split(' / ') -> arr1 = [ "1, 2, 3", "cs", "backend" ]
        arr1[0].split(',') -> arr2 = [ "1", " 2", " 3" ]
         */

        String[] indicesAndTags = args.split(" / ");
        String[] indicesStrings = indicesAndTags[0].split(",");

        if (indicesAndTags.length < 2) {
            // this means that either indices or tags are fully missing
            throw new ParseException("Error: Missing one or more required fields. \n"
                    + "Format: deletetag INDEX, ... / TAG [/ TAG]");
        }

        try {
            // parse indices
            List<Index> indices = new ArrayList<>();

            for (String indexStr : indicesStrings) {
                indices.add(ParserUtil.parseIndex(indexStr)); // trimming is handled inside parseIndex
            }

            // parse tags
            List<String> tagStrings = new ArrayList<>();
            for (int i = 1; i < indicesAndTags.length; i++) {
                tagStrings.add(indicesAndTags[i].trim());
            }

            Set<Tag> tags = ParserUtil.parseTags(tagStrings);

            return new DeleteTagCommand(indices, tags);

        } catch (ParseException pe) {
            throw new ParseException("Error: Format is invalid.\n" + DeleteTagCommand.MESSAGE_FORMAT, pe);
        }
    }
}
