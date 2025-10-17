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
 * Adds a tag to a member (person) identified by its index in the current filtered list.
 * <p>
 * All other fields of the member are preserved. If the tag already exists on the member,
 * this command is effectively a no-op on the tag set.
 * </p>
 *
 * <p>Command word: {@code tag}</p>
 * <p>Usage:</p>
 * <pre>
 *   tag INDEX TAG
 *   e.g., tag 1 Treasurer
 * </pre>
 */
public class TagCommand extends Command {
    /** Command word used to invoke this command. */
    public static final String COMMAND_WORD = "tag";

    /** Usage message shown when the input format is invalid. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to the person identified by the index.\n"
            + "Parameters: INDEX TAG\n"
            + "Example: " + COMMAND_WORD + " 1 friend";

    /** Success message template. */
    public static final String MESSAGE_SUCCESS = "Tag added: %1$s";

    /** Zero-based index into the current filtered list. */
    private final int index;

    /** Tag to add to the selected member. */
    private final Tag tag;

    /**
     * Creates a {@code TagCommand}.
     *
     * @param index Zero-based index of the member in the filtered list.
     * @param tag   Tag to add to the member. Must not be {@code null}.
     */
    public TagCommand(int index, Tag tag) {
        this.index = index;
        this.tag = tag;
    }

    /**
     * Executes the command by adding {@code tag} to the member at {@code index}.
     * The updated member is constructed by copying all existing fields and replacing
     * the tag set with a set containing the new tag in addition to existing tags.
     *
     * @param model The {@link Model} which the command operates on. Must not be {@code null}.
     * @return A {@link CommandResult} describing the outcome.
     * @throws CommandException If {@code index} is out of bounds.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (index < 0 || index >= model.getFilteredPersonList().size()) {
            throw new CommandException("The person index provided is invalid");
        }

        Person personToTag = model.getFilteredPersonList().get(index);

        // Build updated tag set
        Set<Tag> updatedTags = new HashSet<>(personToTag.getTags());
        updatedTags.add(tag);

        // Reconstruct Person, preserving all other fields
        Person updatedPerson = new Person(
                personToTag.getName(),
                personToTag.getPhone(),
                personToTag.getEmail(),
                personToTag.getYearOfStudy(),
                personToTag.getFaculty(),
                personToTag.getAddress(),
                updatedTags,
                personToTag.isPresent(),
                personToTag.getPoints()
        );

        model.setPerson(personToTag, updatedPerson);
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
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand otherCommand = (TagCommand) other;
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
