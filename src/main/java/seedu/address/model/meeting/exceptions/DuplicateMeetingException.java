package seedu.address.model.meeting.exceptions;

/**
 * Signals that the operation will result in duplicate Meetings
 * (Meetings are considered duplicates if they have the same description and date).
 */
public class DuplicateMeetingException extends RuntimeException {

}
