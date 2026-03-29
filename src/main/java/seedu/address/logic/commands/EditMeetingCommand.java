package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_INVALID_PERSON_INDEX;
import static seedu.address.logic.commands.AddMeetingCommand.MESSAGE_MEETING_ALREADY_EXISTS;
import static seedu.address.logic.commands.DeleteMeetingCommand.MESSAGE_INVALID_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_PERSON_TO_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_PERSON_FROM_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.exceptions.DuplicateMeetingException;
import seedu.address.model.person.Person;

/**
 * Edits the details of an existing meeting in the address book.
 */
public class EditMeetingCommand extends Command {

    public static final String COMMAND_WORD = "editmeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the meeting identified "
            + "by the index number used in the displayed meeting list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Participants can be added or deleted based on their positions in the contact list.\n"
            + "E.g " + PREFIX_ADD_PERSON_TO_MEETING_INDEX + "2 will add the second person in the contact list to the meeting. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_MEETING_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_MEETING_DATE + "DATE] "
            + "[" + PREFIX_ADD_PERSON_TO_MEETING_INDEX + "PARTICIPANT_INDEX]... "
            + "[" + PREFIX_DELETE_PERSON_FROM_MEETING_INDEX + "PARTICIPANT_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " 2 "
            + PREFIX_MEETING_DESCRIPTION + "Team Sync "
            + PREFIX_MEETING_DATE + "2026-04-01 "
            + PREFIX_ADD_PERSON_TO_MEETING_INDEX + "3 5 "
            + PREFIX_DELETE_PERSON_FROM_MEETING_INDEX + "2";

    public static final String MESSAGE_EDIT_MEETING_SUCCESS = "Edited meeting(s): %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private final Set<Index> meetingIndices;
    private final EditMeetingDescriptor editMeetingDescriptor;

    /**
     * Creates an EditMeetingCommand to edit the specified {@code Meeting}s
     *
     * @param meetingIndices The indices of the meeting in the list to edit
     * @param editMeetingDescriptor The details to edit the meeting with
     */
    public EditMeetingCommand(Set<Index> meetingIndices, EditMeetingDescriptor editMeetingDescriptor) {
        requireNonNull(meetingIndices);
        requireNonNull(editMeetingDescriptor);

        this.meetingIndices = meetingIndices;
        this.editMeetingDescriptor = new EditMeetingDescriptor(editMeetingDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Meeting> lastShownMeetingList = model.getFilteredMeetingList();

        Set<Meeting> meetingsToEdit = new HashSet<>();
        Set<Index> editedMeetingIndices = new HashSet<>();
        editMeetingDescriptor.resolveParticipantIds(model);

        for (Index index : meetingIndices) {
            if (index.getZeroBased() >= lastShownMeetingList.size()) {
                throw new CommandException(String.format(MESSAGE_INVALID_MEETING_INDEX, index.getOneBased()));
            }
            meetingsToEdit.add(lastShownMeetingList.get(index.getZeroBased()));
        }

        for (Index index : meetingIndices) {
            Meeting meetingToEdit = lastShownMeetingList.get(index.getZeroBased());
            Meeting editedMeeting = createEditedMeeting(meetingToEdit, editMeetingDescriptor);

            try {
                model.setMeeting(meetingToEdit, editedMeeting);
                editedMeetingIndices.add(index);
            } catch (DuplicateMeetingException e) {
                throw new CommandException(MESSAGE_MEETING_ALREADY_EXISTS);
            }
        }
        return new CommandResult(String.format(MESSAGE_EDIT_MEETING_SUCCESS,
                formatMeetingIndices(editedMeetingIndices)));
    }

    /**
     * Creates and returns a {@code Meeting} with the details of {@code meetingToEdit}
     * edited with {@code editMeetingDescriptor}.
     */
    private static Meeting createEditedMeeting(Meeting meetingToEdit,
                                               EditMeetingCommand.EditMeetingDescriptor editMeetingDescriptor) {
        assert meetingToEdit != null;

        String updatedDescription = editMeetingDescriptor.getDescription()
                .orElse(meetingToEdit.getDescription());
        LocalDate updatedDate = editMeetingDescriptor.getDate()
                .orElse(meetingToEdit.getDate());

        Set<UUID> updatedParticipantsId = new HashSet<>(meetingToEdit.getParticipantsID());

        editMeetingDescriptor.getPeopleToAddId()
                .ifPresent(updatedParticipantsId::addAll);

        editMeetingDescriptor.getPeopleToDeleteId()
                .ifPresent(updatedParticipantsId::removeAll);

        return new Meeting(updatedDescription, updatedDate, updatedParticipantsId);
    }

    /**
     * Formats the meeting indices into a string to be used for {@code MESSAGE_DELETE_MEETING_SUCCESS}.
     */
    private String formatMeetingIndices(Set<Index> meetingIndices) {
        return meetingIndices.stream()
                .map(i -> String.valueOf(i.getOneBased()))
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof EditMeetingCommand)) return false;

        EditMeetingCommand otherCommand = (EditMeetingCommand) other;

        return meetingIndices.equals(otherCommand.meetingIndices)
                && editMeetingDescriptor.equals(otherCommand.editMeetingDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("meetingIndices", meetingIndices)
                .add("editMeetingDescriptor", editMeetingDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the meeting with. Each non-empty field value will replace the
     * corresponding field value of the meeting.
     */
    public static class EditMeetingDescriptor {
        private String description;
        private LocalDate date;
        private Set<UUID> participantsID;

        private Set<Index> peopleIndicesToAdd;
        private Set<Index> peopleIndicesToDelete;

        private Set<UUID> peopleToAddId;
        private Set<UUID> peopleToDeleteId;

        public EditMeetingDescriptor() {}

        public EditMeetingDescriptor(EditMeetingDescriptor toCopy) {
            setDescription(toCopy.description);
            setDate(toCopy.date);
            setParticipantsID(toCopy.participantsID);
            setPeopleIndicesToAdd(toCopy.peopleIndicesToAdd);
            setPeopleIndicesToDelete(toCopy.peopleIndicesToDelete);
            setPeopleToAddId(toCopy.peopleToAddId);
            setPeopleToDeleteId(toCopy.peopleToDeleteId);
        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(
                    description, date, participantsID,
                    peopleIndicesToAdd, peopleIndicesToDelete,
                    peopleToAddId, peopleToDeleteId);
        }

        /**
         * Resolves set of participant indices given into a set of
         * their actual {@code id} given the {@code model} provided, and stores them
         * as variables in the class as {@code peopleToAddId} and {@code peopleToDeleteId}.
         */
        public void resolveParticipantIds(Model model) throws CommandException {
            List<Person> persons = model.getFilteredPersonList();

            this.peopleToAddId = resolveIndicesToIds(peopleIndicesToAdd, persons);
            this.peopleToDeleteId = resolveIndicesToIds(peopleIndicesToDelete, persons);
        }

        /**
         * Returns a set of participant {@code id} given a set of {@code indices}
         * that represent the position of the participants in {@code persons}.
         *
         * @param indices Indexes of the persons to find in {@code persons}.
         * @param persons List to search for the participants to get the {@code id} from.
         */
        private Set<UUID> resolveIndicesToIds(Set<Index> indices, List<Person> persons)
                throws CommandException {
            if (indices == null) {
                return null;
            }

            Set<UUID> resolvedIds = new HashSet<>();
            for (Index index : indices) {
                if (index.getZeroBased() >= persons.size()) {
                    throw new CommandException(MESSAGE_INVALID_PERSON_INDEX);
                }
                resolvedIds.add(persons.get(index.getZeroBased()).getId());
            }
            return resolvedIds;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public void setParticipantsID(Set<UUID> participantsID) {
            this.participantsID = (participantsID != null) ? new HashSet<>(participantsID) : null;
        }

        public void setPeopleIndicesToAdd(Set<Index> peopleIndicesToAdd) {
            this.peopleIndicesToAdd = (peopleIndicesToAdd != null) ? new HashSet<>(peopleIndicesToAdd) : null;
        }

        public void setPeopleIndicesToDelete(Set<Index> peopleIndicesToDelete) {
            this.peopleIndicesToDelete = (peopleIndicesToDelete != null) ? new HashSet<>(peopleIndicesToDelete) : null;
        }

        public void setPeopleToAddId(Set<UUID> peopleToAddId) {
            this.peopleToAddId = (peopleToAddId != null) ? new HashSet<>(peopleToAddId) : null;
        }

        public void setPeopleToDeleteId(Set<UUID> peopleToDeleteId) {
            this.peopleToDeleteId = (peopleToDeleteId != null) ? new HashSet<>(peopleToDeleteId) : null;
        }

        public Optional<String> getDescription() {
            return Optional.ofNullable(description);
        }

        public Optional<LocalDate> getDate() {
            return Optional.ofNullable(date);
        }

        public Optional<Set<UUID>> getParticipantsID() {
            return (participantsID != null)
                    ? Optional.of(Collections.unmodifiableSet(participantsID))
                    : Optional.empty();
        }

        public Optional<Set<Index>> getPeopleIndicesToAdd() {
            return (peopleIndicesToAdd != null)
                    ? Optional.of(Collections.unmodifiableSet(peopleIndicesToAdd))
                    : Optional.empty();
        }

        public Optional<Set<Index>> getPeopleIndicesToDelete() {
            return (peopleIndicesToDelete != null)
                    ? Optional.of(Collections.unmodifiableSet(peopleIndicesToDelete))
                    : Optional.empty();
        }

        public Optional<Set<UUID>> getPeopleToAddId() {
            return (peopleToAddId != null)
                    ? Optional.of(Collections.unmodifiableSet(peopleToAddId))
                    : Optional.empty();
        }

        public Optional<Set<UUID>> getPeopleToDeleteId() {
            return (peopleToDeleteId != null)
                    ? Optional.of(Collections.unmodifiableSet(peopleToDeleteId))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof EditMeetingDescriptor)) return false;
            EditMeetingDescriptor o = (EditMeetingDescriptor) other;
            return Objects.equals(description, o.description)
                    && Objects.equals(date, o.date)
                    && Objects.equals(participantsID, o.participantsID)
                    && Objects.equals(peopleIndicesToAdd, o.peopleIndicesToAdd)
                    && Objects.equals(peopleIndicesToDelete, o.peopleIndicesToDelete)
                    && Objects.equals(peopleToAddId, o.peopleToAddId)
                    && Objects.equals(peopleToDeleteId, o.peopleToDeleteId);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("description", description)
                    .add("date", date)
                    .add("participantsID", participantsID)
                    .add("peopleIndicesToAdd", peopleIndicesToAdd)
                    .add("peopleIndicesToDelete", peopleIndicesToDelete)
                    .add("peopleToAddId", peopleToAddId)
                    .add("peopleToDeleteId", peopleToDeleteId)
                    .toString();
        }
    }
}
