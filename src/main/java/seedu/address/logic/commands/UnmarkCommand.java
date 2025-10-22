package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
 * Marks a person as absent identified using it's displayed index from the address book.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "absent";

    /**
     * Usage message for help and documentation.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the person identified by the index number as absent.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Member '%1$s' marked absent.";
    public static final String MESSAGE_ALREADY_ABSENT_NOOP = "Member '%1$s' is already absent. No changes made.";

    private final Index targetIndex;

    /**
     * Constructs an UnmarkCommand to mark a member at the specified index as absent.
     *
     * @param targetIndex Index of the member to unmark (1-based as shown to user).
     */
    public UnmarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the unmark command: marks the member at the specified index as absent.
     * @param model The model containing the member list.
     * @return CommandResult with success message and unmarked member details.
     * @throws CommandException if the index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
        }

        Person personToUnmark = lastShownList.get(targetIndex.getZeroBased());

        // Preserve existing tags; do not manage a special presence tag
        Set<Tag> preservedTags = personToUnmark.getTags();

        // Create a new Person with isPresent set to false and preserved points
        Person unmarkedPerson = new Person(
            personToUnmark.getName(),
            personToUnmark.getPhone(),
            personToUnmark.getEmail(),
            personToUnmark.getYearOfStudy(),
            personToUnmark.getFaculty(),
            personToUnmark.getAddress(),
            preservedTags,
            false,
            personToUnmark.getPoints()
        );

        model.setPerson(personToUnmark, unmarkedPerson);
        return new CommandResult(String.format(
            MESSAGE_UNMARK_PERSON_SUCCESS,
            personToUnmark.getName()
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return targetIndex.equals(otherUnmarkCommand.targetIndex);
    }

    /**
     * Returns a string representation of the UnmarkCommand.
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
