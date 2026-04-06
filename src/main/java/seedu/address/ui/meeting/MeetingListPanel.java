package seedu.address.ui.meeting;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.ui.UiPart;

/**
 * Panel containing the list of persons.
 */
public class MeetingListPanel extends UiPart<Region> {
    private static final String FXML = "MeetingListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(MeetingListPanel.class);

    @FXML
    private ListView<Meeting> meetingListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public MeetingListPanel(ObservableList<Meeting> meetingList, Model model) {
        super(FXML);
        meetingListView.setItems(meetingList);
        meetingListView.setCellFactory(listView -> new MeetingListPanel.MeetingListViewCell(model));
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class MeetingListViewCell extends ListCell<Meeting> {
        private final Model model;

        public MeetingListViewCell(Model model) {
            this.model = model;
        }

        @Override
        protected void updateItem(Meeting meeting, boolean empty) {
            super.updateItem(meeting, empty);

            if (empty || meeting == null) {
                setGraphic(null);
                setText(null);
            } else {
                Set<Person> participants = new HashSet<>();
                meeting.getParticipantsIDs().forEach(id -> participants.add(model.getPerson(id)));

                setGraphic(new MeetingCard(meeting, getIndex() + 1, participants).getRoot());
            }
        }
    }
}
