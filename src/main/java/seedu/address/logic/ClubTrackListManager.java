// New helper to centralize list-level storage operations
package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ClubTrack;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyClubTrack;
import seedu.address.storage.Storage;

/**
 * Helper/service class that encapsulates switching and removing address book list files.
 * Keeps file-level logic out of {@link LogicManager} to preserve single responsibility.
 */
public class ClubTrackListManager {

    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    private static final Logger logger = LogsCenter.getLogger(ClubTrackListManager.class);

    private final Storage storage;

    public ClubTrackListManager(Storage storage) {
        this.storage = storage;
    }

    /**
     * Switches the given model to use the list identified by {@code listName}.
     * If the corresponding file exists, it is loaded; otherwise a new empty file is created.
     */
    public void switchToList(String listName, Model model) throws CommandException {
        requireNonNull(listName);
        requireNonNull(model);

        Path filePath = Paths.get("data", listName + ".json");
        try {
            Optional<ReadOnlyClubTrack> data = storage.readClubTrack(filePath);
            if (data.isPresent()) {
                model.setClubTrack(data.get());
            } else {
                model.setClubTrack(new ClubTrack());
                storage.saveClubTrack(model.getClubTrack(), filePath);
            }
            model.setClubTrackFilePath(filePath);
        } catch (DataLoadingException dle) {
            logger.warning("Failed to load list at " + filePath + ". Starting with empty list.");
            model.setClubTrack(new ClubTrack());
            try {
                storage.saveClubTrack(model.getClubTrack(), filePath);
            } catch (IOException ioe) {
                throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
            }
            model.setClubTrackFilePath(filePath);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }

    /**
     * Removes the list file identified by {@code listName}. If it was the currently loaded list,
     * reverts the model to the default address book file without overwriting existing default data.
     */
    public void removeList(String listName, Model model) throws CommandException {
        requireNonNull(listName);
        requireNonNull(model);

        Path filePath = Paths.get("data", listName + ".json");

        // Prevent removal of the default data file; this file must always exist.
        Path defaultPath = Paths.get("data", "default.json");
        if (filePath.equals(defaultPath)) {
            throw new CommandException("Cannot remove the default list 'default.json'.");
        }

        try {
            java.nio.file.Files.deleteIfExists(filePath);
            // If the removed list was the currently loaded one, revert to default
            if (filePath.equals(model.getClubTrackFilePath())) {
                try {
                    Optional<ReadOnlyClubTrack> defaultData = storage.readClubTrack(defaultPath);
                    if (defaultData.isPresent()) {
                        model.setClubTrack(defaultData.get());
                    } else {
                        model.setClubTrack(new ClubTrack());
                        storage.saveClubTrack(model.getClubTrack(), defaultPath);
                    }
                    model.setClubTrackFilePath(defaultPath);
                } catch (DataLoadingException dle) {
                    logger.warning("Default clubtrack at " + defaultPath + " could not be loaded. "
                            + "Starting with an empty in-memory list without overwriting the file.");
                    model.setClubTrack(new ClubTrack());
                    model.setClubTrackFilePath(defaultPath);
                }
            }
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }
}
