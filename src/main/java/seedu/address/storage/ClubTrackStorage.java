package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ClubTrack;
import seedu.address.model.ReadOnlyClubTrack;

/**
 * Represents a storage for {@link ClubTrack}.
 */
public interface ClubTrackStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getClubTrackFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyClubTrack}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyClubTrack> readClubTrack() throws DataLoadingException;

    /**
     * @see #getClubTrackFilePath()
     */
    Optional<ReadOnlyClubTrack> readClubTrack(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyClubTrack} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveClubTrack(ReadOnlyClubTrack addressBook) throws IOException;

    /**
     * @see #saveClubTrack(ReadOnlyClubTrack)
     */
    void saveClubTrack(ReadOnlyClubTrack addressBook, Path filePath) throws IOException;

}
