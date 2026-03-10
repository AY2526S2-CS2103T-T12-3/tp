package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAnyNonNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CollectionUtilTest {
    @Test
    public void requireAllNonNullVarargs() {
        // no arguments
        assertRequireAllNonNullNoThrow();

        // any non-empty argument list
        assertRequireAllNonNullNoThrow(new Object(), new Object());
        assertRequireAllNonNullNoThrow("test");
        assertRequireAllNonNullNoThrow("");

        // argument lists with just one null at the beginning
        assertRequireAllNonNullThrows((Object) null);
        assertRequireAllNonNullThrows(null, "", new Object());
        assertRequireAllNonNullThrows(null, new Object(), new Object());

        // argument lists with nulls in the middle
        assertRequireAllNonNullThrows(new Object(), null, null, "test");
        assertRequireAllNonNullThrows("", null, new Object());

        // argument lists with one null as the last argument
        assertRequireAllNonNullThrows("", new Object(), null);
        assertRequireAllNonNullThrows(new Object(), new Object(), null);

        // null reference
        assertRequireAllNonNullThrows((Object[]) null);

        // confirms nulls inside lists in the argument list are not considered
        List<Object> containingNull = Arrays.asList((Object) null);
        assertRequireAllNonNullNoThrow(containingNull, new Object());
    }

    @Test
    public void requireAnyNonNullVarargs() {
        // no arguments
        assertRequireAnyNonNullNoThrow();

        // any non-empty argument list
        assertRequireAnyNonNullNoThrow(new Object(), new Object());
        assertRequireAnyNonNullNoThrow("test");
        assertRequireAnyNonNullNoThrow("");

        // argument lists with just one null at the beginning
        assertRequireAnyNonNullThrows((Object) null);
        assertRequireAnyNonNullNoThrow(null, "", new Object());
        assertRequireAnyNonNullNoThrow(null, new Object(), new Object());

        // argument lists with nulls in the middle
        assertRequireAnyNonNullNoThrow(new Object(), null, null, "test");
        assertRequireAnyNonNullNoThrow("", null, new Object());

        // argument lists with one null as the last argument
        assertRequireAnyNonNullNoThrow("", new Object(), null);
        assertRequireAnyNonNullNoThrow(new Object(), new Object(), null);

        // all null
        assertRequireAnyNonNullThrows(null, null, null);

        // null reference
        assertRequireAnyNonNullThrows((Object[]) null);

        // confirms nulls inside lists in the argument list are not considered
        List<Object> containingNull = Arrays.asList((Object) null);
        assertRequireAnyNonNullNoThrow(containingNull, new Object());
    }

    @Test
    public void requireAllNonNullCollection() {
        // lists containing nulls in the front
        assertRequireAllNonNullThrows(Arrays.asList((Object) null));
        assertRequireAllNonNullThrows(Arrays.asList(null, new Object(), ""));

        // lists containing nulls in the middle
        assertRequireAllNonNullThrows(Arrays.asList("spam", null, new Object()));
        assertRequireAllNonNullThrows(Arrays.asList("spam", null, "eggs", null, new Object()));

        // lists containing nulls at the end
        assertRequireAllNonNullThrows(Arrays.asList("spam", new Object(), null));
        assertRequireAllNonNullThrows(Arrays.asList(new Object(), null));

        // null reference
        assertRequireAllNonNullThrows((Collection<Object>) null);

        // empty list
        assertRequireAllNonNullNoThrow(Collections.emptyList());

        // list with all non-null elements
        assertRequireAllNonNullNoThrow(Arrays.asList(new Object(), "ham", Integer.valueOf(1)));
        assertRequireAllNonNullNoThrow(Arrays.asList(new Object()));

        // confirms nulls inside nested lists are not considered
        List<Object> containingNull = Arrays.asList((Object) null);
        assertRequireAllNonNullNoThrow(Arrays.asList(containingNull, new Object()));
    }

    @Test
    public void isAnyNonNull() {
        assertFalse(CollectionUtil.isAnyNonNull());
        assertFalse(CollectionUtil.isAnyNonNull((Object) null));
        assertFalse(CollectionUtil.isAnyNonNull((Object[]) null));
        assertTrue(CollectionUtil.isAnyNonNull(new Object()));
        assertTrue(CollectionUtil.isAnyNonNull(new Object(), null));
    }

    /**
     * Asserts that {@code CollectionUtil#requireAllNonNull(Object...)} throw {@code NullPointerException}
     * if {@code objects} or any element of {@code objects} is null.
     */
    private void assertRequireAllNonNullThrows(Object... objects) {
        assertThrows(NullPointerException.class, () -> requireAllNonNull(objects));
    }

    /**
     * Asserts that {@code CollectionUtil#requireAllNonNull(Collection<?>)} throw {@code NullPointerException}
     * if {@code collection} or any element of {@code collection} is null.
     */
    private void assertRequireAllNonNullThrows(Collection<?> collection) {
        assertThrows(NullPointerException.class, () -> requireAllNonNull(collection));
    }

    /**
     * Asserts that {@code CollectionUtil#requireAnyNonNull(Object...)} throw {@code NullPointerException}
     * if {@code objects} or all elements of {@code objects} is null.
     */
    private void assertRequireAnyNonNullThrows(Object... objects) {
        assertThrows(NullPointerException.class, () -> requireAnyNonNull(objects));
    }

    private void assertRequireAllNonNullNoThrow(Object... objects) {
        requireAllNonNull(objects);
    }

    private void assertRequireAllNonNullNoThrow(Collection<?> collection) {
        requireAllNonNull(collection);
    }

    private void assertRequireAnyNonNullNoThrow(Object... objects) {
        requireAnyNonNull(objects);
    }
}
