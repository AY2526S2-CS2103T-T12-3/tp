package seedu.address.ui.meeting;

import java.time.LocalDate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.ui.UiPart;
import seedu.address.ui.listutil.ListItem;
import seedu.address.ui.listutil.SeparatorItem;

/**
 * Panel containing the list of meetings.
 */
public class MeetingListPanel extends UiPart<Region> {
    private static final String FXML = "MeetingListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(MeetingListPanel.class);

    @FXML
    private ListView<ListItem> meetingListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public MeetingListPanel(ObservableList<Meeting> meetingList, Model model) {
        super(FXML);

        meetingList.addListener((ListChangeListener<Meeting>) change ->
                buildListWithSeparators(meetingList, model));

        buildListWithSeparators(meetingList, model);
        meetingListView.setCellFactory(listView -> new MeetingListViewCell());
    }

    /** Populates the {@code meetingListView} with the specified meetings. */
    private void buildListWithSeparators(ObservableList<Meeting> meetingList, Model model) {
        ObservableList<ListItem> items = FXCollections.observableArrayList();

        items.add(new SeparatorItem("Upcoming Meetings"));

        boolean hasReachedPastMeetings = false;
        for (int i = 0; i < meetingList.size(); i++) {
            Meeting m = meetingList.get(i);

            // When it finds a past meeting for the first time, put a separator.
            if (!hasReachedPastMeetings && m.isBefore(LocalDate.now())) {
                items.add(new SeparatorItem("Past Meetings"));
                hasReachedPastMeetings = true;
            }

            items.add(new MeetingItem(m, model, Index.fromZeroBased(i)));
        }

        meetingListView.setItems(items);
        meetingListView.refresh();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Meeting} using a {@code MeetingCard}.
     * A {@code Separator} can also be inserted in the list.
     */
    private static class MeetingListViewCell extends ListCell<ListItem> {
        @Override
        protected void updateItem(ListItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(item.getView());
                setMouseTransparent(item.isMouseTransparent());
            }
        }
    }
}
