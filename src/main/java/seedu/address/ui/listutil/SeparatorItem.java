package seedu.address.ui.listutil;

import javafx.scene.layout.Region;

/**
 * A separator for visual grouping in a {@code ListView}.
 */
public class SeparatorItem implements ListItem {
    private final String label;

    /**
     * Creates a {@code Separator} with the given {@code label}.
     */
    public SeparatorItem(String label) {
        this.label = label;
    }

    @Override
    public Region getView() {
        return new Separator(label).getRoot();
    }
}
