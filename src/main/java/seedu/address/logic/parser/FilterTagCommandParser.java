package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FilterTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FilterTagCommand object
 */
public class FilterTagCommandParser implements Parser<FilterTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterTagCommand
     * and returns a FilterTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterTagCommand parse(String args) throws ParseException {
        // the following explanatory comments use an example command: "filtertag / cs / backend"

        // args = "/ cs / backend"

        // tagStrings = [ " cs ", " backend" ]
        String[] tagArr = args.split("/");

        try {
            // parse tags
            List<String> tagStrings = new ArrayList<>();
            for (int i = 1; i < tagArr.length; i++) {
                tagStrings.add(tagArr[i].trim());
            }

            Set<Tag> tags = ParserUtil.parseTags(tagStrings);

            return new FilterTagCommand(tags);

        } catch (ParseException pe) {
            throw new ParseException("Error: Format is invalid.\n" + FilterTagCommand.MESSAGE_FORMAT, pe);
        }
    }
}

