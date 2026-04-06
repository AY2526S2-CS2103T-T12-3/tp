package seedu.address.ui.listutil;

import javafx.scene.layout.Region;

/**
 * Represents items that can be put in a {@code ListView}.
 * For example, meeting cards and separators.
 */
public interface ListItem {
    /**
     * Returns the view of this {@code ListItem}.
     */
    Region getView();

    /**
     * Returns true if the list item is NOT interactive.
     */
    boolean isMouseTransparent();
}
