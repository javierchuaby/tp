package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;

/**
 * Removes a tag from a member (person) identified by its index in the current filtered list.
 * <p>
 * All other fields of the member are preserved. If the tag does not exist on the member,
 * this command is effectively a no-op on the tag set.
 * </p>
 *
 * <p>Command word: {@code untag}</p>
 * <p>Usage:</p>
 * <pre>
 *   untag INDEX TAG
 *   e.g., untag 1 Treasurer
 * </pre>
 */
public class UntagCommand extends Command {
    /** Error message when attempting to remove a tag that the person does not have. */
    public static final String MESSAGE_INVALID_TAG = "Invalid tag: %1$s";
    /** Command word used to invoke this command. */
    public static final String COMMAND_WORD = "untag";

    /** Usage message shown when the input format is invalid. */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a tag from the person identified by the index.\n"
            + "Parameters: INDEX TAG\n"
            + "Example: " + COMMAND_WORD + " 1 friend";

    /** Success message template. */
    public static final String MESSAGE_SUCCESS = "Tag removed: %1$s";
    /** Error message when attempting to remove a tag that the person does not have. */
    public static final String MESSAGE_INVALID_TAG = "Invalid tag: %1$s";
    /** Error message when index is out of bounds. */
    private static final String MESSAGE_INVALID_INDEX = "The person index provided is invalid";

    /** Zero-based index into the current filtered list. */
    private final int index;

    /** Tag to remove from the selected member. */
    private final Tag tag;

    /**
     * Creates an {@code UntagCommand}.
     *
     * @param index Zero-based index of the member in the filtered list.
     * @param tag   Tag to remove from the member. Must not be {@code null}.
     */
    public UntagCommand(int index, Tag tag) {
        this.index = index;
        this.tag = tag;
    }

    /**
     * Executes the command by removing {@code tag} from the member at {@code index}.
     * The updated member is constructed by copying all existing fields and replacing
     * the tag set with a set that excludes the removed tag.
     *
     * @param model The {@link Model} which the command operates on. Must not be {@code null}.
     * @return A {@link CommandResult} describing the outcome.
     * @throws CommandException If {@code index} is out of bounds.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (index < 0 || index >= model.getFilteredPersonList().size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        Person personToUntag = model.getFilteredPersonList().get(index);

        // Ensure the person actually has the tag to be removed
        if (!personToUntag.getTags().contains(tag)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAG, tag));
        }

        // Build updated tag set
        Set<Tag> updatedTags = new HashSet<>(personToUntag.getTags());
        updatedTags.remove(tag);

        // Reconstruct Person, preserving all other fields
        Person updatedPerson = new Person(
                personToUntag.getName(),
                personToUntag.getPhone(),
                personToUntag.getEmail(),
                personToUntag.getYearOfStudy(),
                personToUntag.getFaculty(),
                personToUntag.getAddress(),
                updatedTags,
                personToUntag.isPresent(),
                personToUntag.getPoints()
        );

        model.setPerson(personToUntag, updatedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, tag));
    }

    /**
     * Returns {@code true} if both commands target the same index and carry the same tag.
     *
     * @param other The other object to compare with.
     * @return {@code true} if equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UntagCommand)) {
            return false;
        }
        UntagCommand otherCommand = (UntagCommand) other;
        return index == otherCommand.index && Objects.equals(tag, otherCommand.tag);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return Hash code of this command.
     */
    @Override
    public int hashCode() {
        return Objects.hash(index, tag);
    }
}
