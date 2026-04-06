package seedu.address.ui.meeting;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.ui.UiPart;

/**
 * An UI component that displays information of a {@code Meeting}.
 */
public class MeetingCard extends UiPart<Region> {

    private static final String FXML = "MeetingListCard.fxml";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMMM yyyy");

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private FlowPane participants;

    /**
     * Creates a {@code MeetingCard} with the given {@code Meeting}, index, and participants to display.
     */
    public MeetingCard(Meeting meeting, int displayedIndex, Set<Person> participantSet) {
        super(FXML);

        id.setText(displayedIndex + ". ");
        name.setText(meeting.getDescription().description);

        date.setText(formatter.format(meeting.getDate().date));

        // Clear any existing children first (safety)
        participants.getChildren().clear();

        participantSet.stream()
                .sorted(Comparator.comparing(person -> person.getName().fullName))
                .forEach(person -> {
                    ParticipantCard participantCard = new ParticipantCard(person);
                    participants.getChildren().add(participantCard.getRoot());
                });
    }
}
