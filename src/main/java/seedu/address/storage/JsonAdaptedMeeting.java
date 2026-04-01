package seedu.address.storage;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.person.PersonId;

/**
 * Jackson-friendly version of {@link Meeting}.
 */
class JsonAdaptedMeeting {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Meeting's %s field is missing!";
    public static final String MESSAGE_INVALID_ID_FOUND = "Illegal ID found in meeting, skipping ID: ";
    private static final Logger logger = Logger.getLogger(JsonAdaptedMeeting.class.getName());

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
        this.description = source.getDescription().description;
        this.date = source.getDate().toString();
        this.personIds = source.getParticipantsIDs().stream()
                .map(PersonId::toString)
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
        Description modelDescription = new Description(description);

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        MeetingDate parsedDate = ParserUtil.parseDate(date);

        if (personIds == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "personIds"));
        }

        Set<PersonId> modelPersonIds = new HashSet<>();
        for (String id : personIds) {
            try {
                modelPersonIds.add(new PersonId(id));
            } catch (IllegalArgumentException e) {
                logger.warning(MESSAGE_INVALID_ID_FOUND + id);
            }
        }

        return new Meeting(modelDescription, parsedDate, modelPersonIds);
    }
}
