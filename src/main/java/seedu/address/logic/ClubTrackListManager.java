// New helper to centralize list-level storage operations
package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ClubTrack;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyClubTrack;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Points;
import seedu.address.model.util.SampleDataUtil;
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
                if (UserPrefs.DEFAULT_CLUBTRACK_NAME.equals(listName)) {
                    model.setClubTrack(SampleDataUtil.getSampleAddressBook());
                } else {
                    model.setClubTrack(new ClubTrack());
                }
                storage.saveClubTrack(model.getClubTrack(), filePath);
            }
            model.setClubTrackFilePath(filePath);

            // Lazy cumulative merge: accumulate points across all lists by member identity
            Map<String, Integer> identityToTotalPoints = buildGlobalPointsIndex();
            boolean changed = reconcilePointsWithTotals(model, identityToTotalPoints);
            if (changed) {
                storage.saveClubTrack(model.getClubTrack(), filePath);
            }
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

    /** Builds a map of identity -> total points summed across all data/*.json lists. */
    private Map<String, Integer> buildGlobalPointsIndex() {
        Map<String, Integer> totals = new HashMap<>();
        Path dataDir = Paths.get("data");
        try (DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(dataDir, "*.json")) {
            for (Path path : stream) {
                try {
                    Optional<ReadOnlyClubTrack> ro = storage.readClubTrack(path);
                    if (ro.isEmpty()) {
                        continue;
                    }
                    for (Person p : ro.get().getPersonList()) {
                        String id = getIdentityKey(p);
                        if (id == null) {
                            continue;
                        }
                        totals.merge(id, p.getPoints().getValue(), Integer::sum);
                    }
                } catch (Exception e) {
                    // Skip unreadable/malformed files; continue accumulating from others
                    logger.fine("Skipping file during points index build: " + path + ", reason: " + e.getMessage());
                }
            }
        } catch (IOException ioe) {
            // If data directory cannot be read, treat as zero totals
            logger.fine("Unable to scan data directory for cumulative points: " + ioe.getMessage());
        }
        return totals;
    }

    /** Reconciles current model's persons with provided totals; returns true if any points changed. */
    private boolean reconcilePointsWithTotals(Model model, Map<String, Integer> totals) {
        boolean changed = false;
        for (Person person : model.getClubTrack().getPersonList()) {
            String id = getIdentityKey(person);
            if (id == null) {
                continue;
            }
            Integer total = totals.get(id);
            if (total == null) {
                continue;
            }
            int current = person.getPoints().getValue();
            if (total != current) {
                Person updated = new Person(
                        person.getName(),
                        person.getPhone(),
                        person.getEmail(),
                        person.getYearOfStudy(),
                        person.getFaculty(),
                        person.getAddress(),
                        person.getTags(),
                        person.isPresent(),
                        new Points(total));
                model.setPerson(person, updated);
                changed = true;
            }
        }
        return changed;
    }

    /** Returns a stable identity key for a person: email preferred, else phone. */
    private String getIdentityKey(Person p) {
        if (p.getEmail() != null && p.getEmail().toString() != null) {
            String email = p.getEmail().toString().trim();
            if (!email.isEmpty()) {
                return "E:" + email;
            }
        }
        if (p.getPhone() != null && p.getPhone().toString() != null) {
            String phone = p.getPhone().toString().trim();
            if (!phone.isEmpty()) {
                return "P:" + phone;
            }
        }
        return null;
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
        Path defaultPath = UserPrefs.DEFAULT_CLUBTRACK_PATH;
        if (filePath.equals(defaultPath)) {
            throw new CommandException("Cannot remove the default list '"
                    + UserPrefs.DEFAULT_CLUBTRACK_PATH.getFileName().toString() + "'.");
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
                        model.setClubTrack(SampleDataUtil.getSampleAddressBook());
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
