package seedu.address.model.person;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a unique ID for a Person.
 * For the first constructor where no string is provided, generates a new random ID.
 * For the second constructor where string is provided, validates it as a proper UUID.
 * Guarantees: immutable;
 */
public class PersonId {
    public static final String PERSON_ID_CONSTRAINTS = "ID string cannot be blank or null";
    private static final Logger logger = Logger.getLogger(PersonId.class.getName());

    private final UUID uuid;

    /**
     * Generates a new random PersonId.
     */
    public PersonId() {
        this.uuid = UUID.randomUUID();
        logger.log(Level.INFO, "No ID provided. Generated new PersonId: {0}", uuid);
    }

    /**
     * Wraps an existing string ID.
     *
     * @param idString existing ID string.
     * @throws IllegalArgumentException if idString is null, blank, or not a valid UUID.
     */
    public PersonId(String idString) {
        if (idString == null || idString.isBlank()) {
            throw new IllegalArgumentException(PERSON_ID_CONSTRAINTS);
        }
        this.uuid = UUID.fromString(idString);
    }

    /** Returns the underlying UUID. */
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PersonId && uuid.equals(((PersonId) other).uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
