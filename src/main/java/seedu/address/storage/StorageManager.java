package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyClubTrack;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ClubTrackStorage clubTrackStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code AddressBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(ClubTrackStorage clubTrackStorage, UserPrefsStorage userPrefsStorage) {
        this.clubTrackStorage = clubTrackStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getClubTrackFilePath() {
        return clubTrackStorage.getClubTrackFilePath();
    }

    @Override
    public Optional<ReadOnlyClubTrack> readClubTrack() throws DataLoadingException {
        return readClubTrack(clubTrackStorage.getClubTrackFilePath());
    }

    @Override
    public Optional<ReadOnlyClubTrack> readClubTrack(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return clubTrackStorage.readClubTrack(filePath);
    }

    @Override
    public void saveClubTrack(ReadOnlyClubTrack addressBook) throws IOException {
        saveClubTrack(addressBook, clubTrackStorage.getClubTrackFilePath());
    }

    @Override
    public void saveClubTrack(ReadOnlyClubTrack addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        clubTrackStorage.saveClubTrack(addressBook, filePath);
    }

}
