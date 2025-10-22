package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all persons in ClubTrack.
 * <p>
 * This command resets any filters applied to the member list and displays
 * all pesrons currently stored in the system. It always succeeds, even if the list is empty.
 * <p>
 * Example usage:
 * <pre>
 * {@code list}
 * </pre>
 * Output:
 * <pre>
 * Listed all persons
 * </pre>
 */
public class ListCommand extends Command {

    /** Command word used to invoke the ListCommand. */
    public static final String COMMAND_WORD = "list";

    /** Aliases that can also be used to invoke the command (optional). */
    public static final String[] COMMAND_ALIASES = { "ls" };

    /**
     * Message that describes the proper usage of this command.
     * Displayed when user inputs an invalid format or requests help.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons in the system.\n"
            + "Example: " + COMMAND_WORD;

    /** Message displayed upon successful execution of the command. */
    public static final String MESSAGE_SUCCESS = "Listed all persons";

    /**
     * Executes the list command and returns the result message.
     * <p>
     * Resets any existing filters on the member list by applying
     * {@code PREDICATE_SHOW_ALL_PERSONS}, ensuring all persons are shown.
     *
     * @param model The model which the command operates on. Must not be null.
     * @return A {@code CommandResult} containing feedback to the user.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
    model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
