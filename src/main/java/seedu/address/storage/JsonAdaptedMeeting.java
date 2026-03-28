package seedu.address.storage;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.meeting.Meeting;

/**
 * Jackson-friendly version of {@link Meeting}.
 */
class JsonAdaptedMeeting {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Meeting's %s field is missing!";

    private final String description;
    private final String date;
    private final Set<String> personIds;

    /**
     * Constructs a {@code JsonAdaptedMeeting} with the given details.
     */
    @JsonCreator
    public JsonAdaptedMeeting(@JsonProperty("description") String description,
                              @JsonProperty("date") String date,
                              @JsonProperty("personIds") Set<String> personIds) {
        this.description = description;
        this.date = date;
        this.personIds = personIds;
    }

    /**
     * Converts a given {@code Meeting} into this class for Jackson use.
     */
    public JsonAdaptedMeeting(Meeting source) {
        this.description = source.getDescription();
        this.date = source.getDate().toString();
        this.personIds = source.getParticipantsID().stream()
                .map(UUID::toString)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public Set<String> getPersonIds() {
        return personIds;
    }

    /**
     * Converts this Jackson-friendly adapted meeting object into the model's {@code Meeting} object.
     */
    public Meeting toModelType() throws IllegalValueException {

        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "description"));
        }
        if (!Meeting.isValidDescription(description)) {
            throw new IllegalValueException(Meeting.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        if (!Meeting.isValidDateString(date)) {
            throw new IllegalValueException(Meeting.MESSAGE_DATE_CONSTRAINTS);
        }

        if (personIds == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "personIds"));
        }

        LocalDate parsedDate = ParserUtil.parseDate(date);

        Set<UUID> modelPersonIds = personIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return new Meeting(description, parsedDate, modelPersonIds);
    }
}
