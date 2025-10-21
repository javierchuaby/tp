package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.model.Model;

/**
 * Switches the active address book file (list) to the specified list name.
 */
public class SwitchCommand extends Command {

    public static final String COMMAND_WORD = "switch";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches to the specified list. "
            + "If the list does not exist, a new empty list is created.\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_SUCCESS = "Switched to list: %s";

    private final String listName;

    /**
     * Creates a SwitchCommand for the specified list name.
     *
     * @param listName name of the list to switch to
     */
    public SwitchCommand(String listName) {
        requireNonNull(listName);
        this.listName = listName;
    }

    /**
     * Returns the name of the list to switch to.
     */
    public String getListName() {
        return listName;
    }

    /**
     * Returns the Path on disk corresponding to this list name.
     */
    public Path getFilePath() {
        return Paths.get("data", listName + ".json");
    }

    @Override
    public CommandResult execute(Model model) {
        // The heavy lifting of reading/writing files is performed in LogicManager so here
        // we only update the model's preferred file path. LogicManager will perform the
        // actual load/save when it recognises this command.
        requireNonNull(model);
        model.setAddressBookFilePath(getFilePath());
        return new CommandResult(String.format(MESSAGE_SUCCESS, listName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SwitchCommand)) {
            return false;
        }
        SwitchCommand otherCmd = (SwitchCommand) other;
        return listName.equals(otherCmd.listName);
    }

    @Override
    public int hashCode() {
        return listName.hashCode();
    }
}
