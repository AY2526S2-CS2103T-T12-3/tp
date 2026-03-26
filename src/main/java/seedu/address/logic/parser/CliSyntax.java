package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {
    /*
    Note regarding PREFIX_NAME and PREFIX_NEWTAG being the same:
    We are aware that the two could cause potential confusion. However, we have decided to keep both because:
    1) They are never used in the same commands (so no risk of conflicts/confusion).
    2) NEWTAG is very intuitive (old vs new) and if we use another letter to substitute, it might be unintuitive and
    risk user confusion.
     */

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_SEPARATOR = new Prefix("/");
    public static final Prefix PREFIX_COMMA = new Prefix(",");
    public static final Prefix PREFIX_OLDTAG = new Prefix("o/");
    public static final Prefix PREFIX_NEWTAG = new Prefix("n/");
    public static final Prefix PREFIX_MEETING_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_MEETING_DATE = new Prefix("dt/");
}
