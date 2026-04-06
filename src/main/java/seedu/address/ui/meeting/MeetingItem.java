package seedu.address.ui.meeting;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.layout.Region;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.ui.listutil.ListItem;

/**
 * A list item that contains a meeting card.
 */
public class MeetingItem implements ListItem {
    private final Meeting meeting;
    private final Model model;
    private final Index index;

    /**
     * Creates a {@code MeetingItem} with the given {@code Meeting} and {@code Index}.
     * The participants will be obtained from the person list in the {@code Model}.
     */
    public MeetingItem(Meeting meeting, Model model, Index index) {
        this.meeting = meeting;
        this.model = model;
        this.index = index;
    }

    @Override
    public Region getView() {
        Set<Person> participants = new HashSet<>();
        meeting.getParticipantsIDs().forEach(id -> participants.add(model.getPerson(id)));
        return new MeetingCard(meeting, index.getOneBased(), participants).getRoot();
    }

    @Override
    public boolean isMouseTransparent() {
        return false;
    }
}
