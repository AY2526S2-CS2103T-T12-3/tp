package seedu.address.model.person;

import java.util.UUID;

/**
 * Represents a unique ID for a Person.
 * For the first constructor where no string is provided, generates a new random ID.
 * For the second constructor where string is provided, validates it as a proper UUID.
 * Guarantees: immutable;
 */
public class PersonId {
    public static final String PERSON_ID_CONSTRAINTS = "ID string cannot be blank or null";
    private final UUID id;

    /**
     * Generates a new random PersonId.
     */
    public PersonId() {
        this.id = UUID.randomUUID();
    }

    /**
     * Wraps an existing string ID.
     * @param idString existing ID string
     * @throws IllegalArgumentException if idString is null, blank, or not a valid UUID
     */
    public PersonId(String idString) {
        if (idString == null || idString.isBlank()) {
            throw new IllegalArgumentException(PERSON_ID_CONSTRAINTS);
        }
        this.id = UUID.fromString(idString);
    }

    /** Returns the underlying UUID. */
    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PersonId && id.equals(((PersonId) other).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
