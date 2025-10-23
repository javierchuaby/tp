package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.model.Model;

/**
 * Removes a saved address book file (list) with the given name.
 */
public class RemoveCommand extends Command {

    public static final String COMMAND_WORD = "remove";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the specified list file from disk.\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_SUCCESS = "Removed list: %s";

    private final String listName;

    /**
     * Creates a RemoveCommand for the specified list name.
     *
     * @param listName name of the list to remove
     */
    public RemoveCommand(String listName) {
        requireNonNull(listName);
        this.listName = listName;
    }

    /**
     * Returns the name of the list to remove.
     */
    public String getListName() {
        return listName;
    }

    /**
     * Returns the file path on disk corresponding to this list name.
     */
    public Path getFilePath() {
        return Paths.get("data", listName + ".json");
    }

    @Override
    public CommandResult execute(Model model) {
        // File deletion is handled in LogicManager so that storage and model stay in sync.
        requireNonNull(model);
        return new CommandResult(String.format(MESSAGE_SUCCESS, listName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemoveCommand)) {
            return false;
        }
        RemoveCommand otherCmd = (RemoveCommand) other;
        return listName.equals(otherCmd.listName);
    }

    @Override
    public int hashCode() {
        return listName.hashCode();
    }
}
