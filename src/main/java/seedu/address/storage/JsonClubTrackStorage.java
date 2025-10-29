package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyClubTrack;

/**
 * A class to access AddressBook data stored as a json file on the hard disk.
 */
public class JsonClubTrackStorage implements ClubTrackStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonClubTrackStorage.class);

    private Path filePath;

    public JsonClubTrackStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getClubTrackFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyClubTrack> readClubTrack() throws DataLoadingException {
        return readClubTrack(filePath);
    }

    /**
     * Similar to {@link #readClubTrack()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyClubTrack> readClubTrack(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableClubTrack> jsonAddressBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableClubTrack.class);
        if (!jsonAddressBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonAddressBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveClubTrack(ReadOnlyClubTrack addressBook) throws IOException {
        saveClubTrack(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveClubTrack(ReadOnlyClubTrack)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveClubTrack(ReadOnlyClubTrack addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableClubTrack(addressBook), filePath);
    }

}
