package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;

/**
 * Marks a person as present identified using it's displayed index from the address book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "present";

    /**
     * Usage message for help and documentation.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the person identified by the index number as present.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Member '%1$s' marked present.";

    private final Index targetIndex;

    /**
     * Constructs a MarkCommand to mark a member at the specified index as present.
     *
     * @param targetIndex Index of the member to mark (1-based as shown to user).
     */
    public MarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the mark command: marks the member at the specified index as present.
     * @param model The model containing the member list.
     * @return CommandResult with success message and marked member details.
     * @throws CommandException if the index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());

        // Clone tags and add the "present" tag if not already present
        Set<Tag> updatedTags = new HashSet<>(personToMark.getTags());
        updatedTags.add(new Tag("present"));

        // Create a new Person with isPresent set to true, updated tags, and incremented points
        Person markedPerson = new Person(
            personToMark.getName(),
            personToMark.getPhone(),
            personToMark.getEmail(),
            personToMark.getYearOfStudy(),
            personToMark.getFaculty(),
            personToMark.getAddress(),
            updatedTags,
            true,
            personToMark.getPoints().addPoint()
        );

        model.setPerson(personToMark, markedPerson);
        return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS + " Points awarded: %2$d",
            personToMark.getName(),
            markedPerson.getPoints().getValue()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndex.equals(otherMarkCommand.targetIndex);
    }

    /**
     * Returns a string representation of the MarkCommand.
     *
     * @return String describing the command and target index.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
