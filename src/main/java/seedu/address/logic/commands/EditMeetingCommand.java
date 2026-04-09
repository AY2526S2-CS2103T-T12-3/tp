package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_INVALID_CONTACT_INDEX;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_MEETING_ALREADY_EXISTS;
import static seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_CONTACT_TO_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Description;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.MeetingDate;
import seedu.address.model.meeting.exceptions.DuplicateMeetingException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;

/**
 * Edits the details of an existing meeting in the meeting list.
 */
public class EditMeetingCommand extends Command {

    public static final String COMMAND_WORD = "editmeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the meeting identified by its index in the displayed meeting list.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Participants can be added or removed using their indices in the displayed contact list. "
            + "E.g. " + PREFIX_ADD_CONTACT_TO_MEETING_INDEX
            + "2 adds the 2nd person in the displayed contact list to the meeting.\n"
            + "Format: " + COMMAND_WORD + " MEETING_INDEX "
            + "(" + PREFIX_MEETING_DESCRIPTION + "DESCRIPTION) "
            + "(" + PREFIX_MEETING_DATE + "DATE) "
            + "(" + PREFIX_ADD_CONTACT_TO_MEETING_INDEX
            + "CONTACT_INDEX [, CONTACT_INDEX]...) "
            + "(" + PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX
            + "CONTACT_INDEX [, CONTACT_INDEX]...)\n"
            + "Note: Date must be in YYYY-MM-DD format.\n"
            + "Note: MEETING_INDEX and CONTACT_INDEX must be a positive integer\n"
            + "Example: " + COMMAND_WORD + " 2 "
            + PREFIX_MEETING_DESCRIPTION + "Team Sync "
            + PREFIX_MEETING_DATE + "2026-04-01 "
            + PREFIX_ADD_CONTACT_TO_MEETING_INDEX + "3,5 "
            + PREFIX_DELETE_CONTACT_FROM_MEETING_INDEX + "2";

    public static final String MESSAGE_EDIT_MEETING_SUCCESS = "Edited meeting: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_PERSON_NOT_IN_MEETING =
            "Invalid contact index provided: At least one participant to be removed is not in the meeting.";

    private final Index meetingIndex;
    private final EditMeetingDescriptor editMeetingDescriptor;

    /**
     * Creates an EditMeetingCommand to edit the specified {@code Meeting}.
     *
     * @param meetingIndex The index of the meeting in the displayed meeting list to edit.
     * @param editMeetingDescriptor The details to edit the meeting with.
     */
    public EditMeetingCommand(Index meetingIndex, EditMeetingDescriptor editMeetingDescriptor) {
        requireNonNull(meetingIndex);
        requireNonNull(editMeetingDescriptor);

        this.meetingIndex = meetingIndex;
        this.editMeetingDescriptor = new EditMeetingDescriptor(editMeetingDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Meeting> lastShownMeetingList = model.getFilteredMeetingList();

        editMeetingDescriptor.resolveParticipantIds(model);

        if (meetingIndex.getZeroBased() >= lastShownMeetingList.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_MEETING_INDEX, meetingIndex.getOneBased()));
        }
        Meeting meetingToEdit = lastShownMeetingList.get(meetingIndex.getZeroBased());
        Meeting editedMeeting = createEditedMeeting(meetingToEdit, editMeetingDescriptor);

        try {
            model.setMeeting(meetingToEdit, editedMeeting);
        } catch (DuplicateMeetingException e) {
            throw new CommandException(MESSAGE_MEETING_ALREADY_EXISTS);
        }

        String passedDateNotification = editedMeeting.getDate().getPassedDateNotification();
        return new CommandResult(String.format(MESSAGE_EDIT_MEETING_SUCCESS + passedDateNotification, editedMeeting));
    }

    /**
     * Creates and returns a {@code Meeting} with the details of {@code meetingToEdit}
     * edited with {@code editMeetingDescriptor}.
     *
     * @throws CommandException If a participant being removed is not in the meeting.
     */
    private static Meeting createEditedMeeting(Meeting meetingToEdit,
                                               EditMeetingCommand.EditMeetingDescriptor editMeetingDescriptor)
            throws CommandException {

        assert meetingToEdit != null;

        Description updatedDescription = editMeetingDescriptor.getDescription()
                .orElse(meetingToEdit.getDescription());
        MeetingDate updatedDate = editMeetingDescriptor.getDate()
                .orElse(meetingToEdit.getDate());

        Set<PersonId> updatedParticipantsId = new HashSet<>(meetingToEdit.getParticipantsIDs());

        editMeetingDescriptor.getIdsToAdd()
                .ifPresent(updatedParticipantsId::addAll);

        Optional<Set<PersonId>> idsToDelete = editMeetingDescriptor.getIdsToDelete();
        if (idsToDelete.isPresent()) {
            for (PersonId id : idsToDelete.get()) {
                if (!updatedParticipantsId.contains(id)) {
                    throw new CommandException(MESSAGE_PERSON_NOT_IN_MEETING);
                }
            }
            updatedParticipantsId.removeAll(editMeetingDescriptor.getIdsToDelete().get());
        }

        return new Meeting(updatedDescription, updatedDate, updatedParticipantsId);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditMeetingCommand)) {
            return false;
        }

        EditMeetingCommand otherCommand = (EditMeetingCommand) other;

        return meetingIndex.equals(otherCommand.meetingIndex)
                && editMeetingDescriptor.equals(otherCommand.editMeetingDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("meetingIndex", meetingIndex)
                .add("editMeetingDescriptor", editMeetingDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the meeting with. Each non-empty field value will replace the
     * corresponding field value of the meeting.
     */
    public static class EditMeetingDescriptor {
        private Description description;
        private MeetingDate date;
        private Set<PersonId> participantsID;

        private Set<Index> personIndicesToAdd;
        private Set<Index> personIndicesToDelete;

        private Set<PersonId> idsToAdd;
        private Set<PersonId> idsToDelete;

        public EditMeetingDescriptor() {}

        /**
         * Copies {@code toCopy}'s information.
         */
        public EditMeetingDescriptor(EditMeetingDescriptor toCopy) {
            setDescription(toCopy.description);
            setDate(toCopy.date);
            setParticipantsID(toCopy.participantsID);
            setPersonIndicesToAdd(toCopy.personIndicesToAdd);
            setPersonIndicesToDelete(toCopy.personIndicesToDelete);
            setIdsToAdd(toCopy.idsToAdd);
            setIdsToDelete(toCopy.idsToDelete);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(
                    description, date, participantsID,
                    personIndicesToAdd, personIndicesToDelete,
                    idsToAdd, idsToDelete);
        }

        /**
         * Converts the participant indices stored in this descriptor into the corresponding
         * participant IDs using the provided {@code model}. The resulting IDs are stored
         * internally as {@code idsToAdd} and {@code idsToDelete}.
         *
         * @param model The {@code Model} containing the list of persons to resolve indices from.
         * @throws CommandException Thrown if any index is out of bounds of the person list.
         */
        public void resolveParticipantIds(Model model) throws CommandException {
            List<Person> persons = model.getFilteredPersonList();

            this.idsToAdd = resolveIndicesToIds(personIndicesToAdd, persons);
            this.idsToDelete = resolveIndicesToIds(personIndicesToDelete, persons);
        }

        /**
         * Resolves a set of {@code Index} objects to their corresponding participant {@code PersonId}s
         * from the given list of {@code persons}.
         *
         * @param indices The set of indices representing positions in {@code persons}; may be {@code null}.
         * @param persons The list of persons to resolve the IDs from.
         * @return A set of resolved participant {@code PersonId}s, or {@code null} if {@code indices} is {@code null}.
         * @throws CommandException If any index is invalid (i.e., out of bounds of {@code persons}).
         */
        private Set<PersonId> resolveIndicesToIds(Set<Index> indices, List<Person> persons)
                throws CommandException {
            if (indices == null) {
                return null;
            }

            Set<PersonId> resolvedIds = new HashSet<>();
            for (Index index : indices) {
                if (index.getZeroBased() >= persons.size()) {
                    throw new CommandException(MESSAGE_INVALID_CONTACT_INDEX);
                }
                resolvedIds.add(persons.get(index.getZeroBased()).getId());
            }
            return resolvedIds;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public void setDate(MeetingDate date) {
            this.date = date;
        }

        public void setParticipantsID(Set<PersonId> participantsID) {
            this.participantsID = (participantsID != null) ? new HashSet<>(participantsID) : null;
        }

        public void setPersonIndicesToAdd(Set<Index> personIndicesToAdd) {
            this.personIndicesToAdd = (personIndicesToAdd != null) ? new HashSet<>(personIndicesToAdd) : null;
        }

        public void setPersonIndicesToDelete(Set<Index> personIndicesToDelete) {
            this.personIndicesToDelete = (personIndicesToDelete != null) ? new HashSet<>(personIndicesToDelete) : null;
        }

        public void setIdsToAdd(Set<PersonId> idsToAdd) {
            this.idsToAdd = (idsToAdd != null) ? new HashSet<>(idsToAdd) : null;
        }

        public void setIdsToDelete(Set<PersonId> idsToDelete) {
            this.idsToDelete = (idsToDelete != null) ? new HashSet<>(idsToDelete) : null;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public Optional<MeetingDate> getDate() {
            return Optional.ofNullable(date);
        }

        public Optional<Set<PersonId>> getParticipantsID() {
            return (participantsID != null)
                    ? Optional.of(Collections.unmodifiableSet(participantsID))
                    : Optional.empty();
        }

        public Optional<Set<Index>> getPersonIndicesToAdd() {
            return (personIndicesToAdd != null)
                    ? Optional.of(Collections.unmodifiableSet(personIndicesToAdd))
                    : Optional.empty();
        }

        public Optional<Set<Index>> getPersonIndicesToDelete() {
            return (personIndicesToDelete != null)
                    ? Optional.of(Collections.unmodifiableSet(personIndicesToDelete))
                    : Optional.empty();
        }

        public Optional<Set<PersonId>> getIdsToAdd() {
            return (idsToAdd != null)
                    ? Optional.of(Collections.unmodifiableSet(idsToAdd))
                    : Optional.empty();
        }

        public Optional<Set<PersonId>> getIdsToDelete() {
            return (idsToDelete != null)
                    ? Optional.of(Collections.unmodifiableSet(idsToDelete))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditMeetingDescriptor)) {
                return false;
            }

            EditMeetingDescriptor o = (EditMeetingDescriptor) other;
            return Objects.equals(description, o.description)
                    && Objects.equals(date, o.date)
                    && Objects.equals(participantsID, o.participantsID)
                    && Objects.equals(personIndicesToAdd, o.personIndicesToAdd)
                    && Objects.equals(personIndicesToDelete, o.personIndicesToDelete)
                    && Objects.equals(idsToAdd, o.idsToAdd)
                    && Objects.equals(idsToDelete, o.idsToDelete);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("description", description)
                    .add("date", date)
                    .add("participantsID", participantsID)
                    .add("peopleIndicesToAdd", personIndicesToAdd)
                    .add("peopleIndicesToDelete", personIndicesToDelete)
                    .add("peopleToAddId", idsToAdd)
                    .add("peopleToDeleteId", idsToDelete)
                    .toString();
        }
    }
}
