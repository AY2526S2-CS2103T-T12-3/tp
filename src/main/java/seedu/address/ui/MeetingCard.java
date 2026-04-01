package seedu.address.ui;

import java.util.Comparator;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Meeting}.
 */
public class MeetingCard extends UiPart<Region> {

    private static final String FXML = "MeetingListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Meeting meeting;

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
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public MeetingCard(Meeting meeting, int displayedIndex, Set<Person> participantSet) {
        super(FXML);
        this.meeting = meeting;

        id.setText(displayedIndex + ". ");
        name.setText(meeting.getDescription().description);

        date.setText(meeting.getDate().toString());

        // Clear any existing children first (safety)
        participants.getChildren().clear();

        participantSet.stream()
                .sorted(Comparator.comparing(person -> person.getName().fullName))
                .forEach(person -> {
                    // Create one label with name, phone, email stacked using \n
                    String text = person.getName().fullName + "\n"
                            + person.getPhone().value + "\n"
                            + person.getEmail().value;

                    Label personLabel = new Label(text);

                    // Optional styling
                    personLabel.setStyle("-fx-padding: 3; -fx-border-color: lightgray; -fx-border-radius: 3;");

                    // Add the label to the FlowPane
                    participants.getChildren().add(personLabel);
                });
    }
}
