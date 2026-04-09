package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Meeting's description in the address book.
 * Guarantees: immutable; is not blank.
 */
public class Description {
    public static final String MESSAGE_DESCRIPTION_NON_NULL =
            "Meeting description must not be null";
    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS = "Meeting description should not be blank";

    public final String description;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A meeting description to validate.
     */
    public Description(String description) {
        requireNonNull(description, MESSAGE_DESCRIPTION_NON_NULL);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);

        this.description = description;
    }

    /**
     * Returns true if a given string is not blank.
     */
    public static boolean isValidDescription(String test) {
        return !test.isBlank();
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Description)) {
            return false;
        }
        Description otherDescription = (Description) other;
        return description.equalsIgnoreCase(otherDescription.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
