package seedu.address.ui.meeting;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.ui.UiPart;

/**
 * A UI component that displays information of a {@code Person} in a meeting.
 */
public class ParticipantCard extends UiPart<Region> {

    private static final String FXML = "ParticipantCard.fxml";

    public final Person person;

    @FXML
    private Label participantDetails;

    /**
     * Creates the display for a given {@code Person} in a meeting.
     */
    public ParticipantCard(Person person) {
        super(FXML);
        this.person = person;

        String text = person.getName().fullName + "\n"
                + (person.getPhone() != null ? person.getPhone().value : "") + "\n"
                + (person.getEmail() != null ? person.getEmail().value : "");

        participantDetails.setText(text);
    }
}
