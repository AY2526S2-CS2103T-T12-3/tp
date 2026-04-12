package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}.
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS =
            "Tags names should be alphanumeric and may contain spaces "
                    + "and special characters, except for the forward slash { / }.";
    public static final String VALIDATION_REGEX = "[\\p{Alnum} \\p{Punct}&&[^/]]+";

    public static final String STAR_TAG = "STAR";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);

        if (tagName.equalsIgnoreCase("STAR")) {
            this.tagName = "STAR";
        } else {
            this.tagName = tagName;
        }
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        Boolean matchesRegex = test.matches(VALIDATION_REGEX);
        Boolean isEmpty = test.trim().isEmpty();
        return matchesRegex && !isEmpty;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equalsIgnoreCase(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
