package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        if (person.getPhone() != null) {
            phone.setText(person.getPhone().value);
        } else {
            phone.setManaged(false);
            phone.setVisible(false);
        }

        if (person.getEmail() != null) {
            email.setText(person.getEmail().value);
        } else {
            email.setManaged(false);
            email.setVisible(false);
        }

        Tag starTag = new Tag(Tag.STAR_TAG);

        // show the STAR tag first
        person.getTags().stream()
                .filter(tag -> tag.equals(starTag))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setStyle("-fx-background-color: gold; -fx-text-fill: black;");
                    tags.getChildren().add(tagLabel);
                });

        // then show the rest of the tags
        person.getTags().stream()
                .filter(tag -> !tag.equals(starTag))
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

    }
}
