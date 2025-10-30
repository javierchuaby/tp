package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ClubTrack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyClubTrack;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.Storage;

/**
 * Unit tests for {@link ClubTrackListManager}.
 */
public class ClubTrackListManagerTest {

    private static final Path DEFAULT_PATH = Paths.get("data", "addressbook.json");

    private ClubTrackListManager listManager;
    private FakeStorage storage;
    private Model model;

    @BeforeEach
    public void setUp() {
        storage = new FakeStorage();
        listManager = new ClubTrackListManager(storage);
        model = new ModelManager();
    }

    @Test
    public void switchToList_existingFile_loadsData() throws Exception {
        ReadOnlyClubTrack sample = SampleDataUtil.getSampleAddressBook();
        Path p = Paths.get("data", "friends.json");
        storage.put(p, sample);

        listManager.switchToList("friends", model);

        assertEquals(sample, model.getClubTrack());
        assertEquals(p, model.getClubTrackFilePath());
    }

    @Test
    public void switchToList_nonExistingFile_createsAndSavesEmpty() throws Exception {
        Path p = Paths.get("data", "newlist.json");

        listManager.switchToList("newlist", model);

        // model updated to empty address book
        assertTrue(model.getClubTrack().getPersonList().isEmpty());
        assertEquals(p, model.getClubTrackFilePath());

        // storage should have saved an empty address book for that path
        ReadOnlyClubTrack saved = storage.getSaved(p);
        assertEquals(model.getClubTrack(), saved);
    }

    @Test
    public void removeList_whenCurrent_revertsToExistingDefault() throws Exception {
        // Prepare default address book in storage
        ReadOnlyClubTrack defaultAb = SampleDataUtil.getSampleAddressBook();
        storage.put(DEFAULT_PATH, defaultAb);

        // Set model currently using a list file
        Path removedPath = Paths.get("data", "toremove.json");
        model.setClubTrackFilePath(removedPath);
        model.setClubTrack(new ClubTrack());

        listManager.removeList("toremove", model);

        // model should now be using default data loaded from storage
        assertEquals(defaultAb, model.getClubTrack());
        assertEquals(DEFAULT_PATH, model.getClubTrackFilePath());

        // ensure default was not overwritten (FakeStorage will only record saves explicitly)
        assertFalse(storage.wasSaved(DEFAULT_PATH));
    }

    @Test
    public void removeList_whenDefaultMissing_createsEmptyDefault() throws Exception {
        // Ensure default not present in storage
        storage.clear();

        Path removedPath = Paths.get("data", "toremove2.json");
        model.setClubTrackFilePath(removedPath);
        model.setClubTrack(new ClubTrack());

        listManager.removeList("toremove2", model);

        // model should point to the default path
        assertEquals(DEFAULT_PATH, model.getClubTrackFilePath());
        // storage should have saved an empty address book for default
        assertTrue(storage.wasSaved(DEFAULT_PATH));
        ReadOnlyClubTrack savedDefault = storage.getSaved(DEFAULT_PATH);
        assertEquals(model.getClubTrack(), savedDefault);
    }

    /*
     * Minimal fake Storage implementation used for testing to simulate persisted address book files
     * without touching the real filesystem. Only the methods used by AddressBookListManager are
     * implemented meaningfully; others are left as simple stubs.
     */
    private static class FakeStorage implements Storage {
        private final Map<Path, ReadOnlyClubTrack> store = new HashMap<>();
        private final Map<Path, ReadOnlyClubTrack> saved = new HashMap<>();

        void put(Path p, ReadOnlyClubTrack ab) {
            store.put(p, ab);
        }

        ReadOnlyClubTrack getSaved(Path p) {
            return saved.get(p);
        }

        boolean wasSaved(Path p) {
            return saved.containsKey(p);
        }

        void clear() {
            store.clear();
            saved.clear();
        }

        @Override
        public Optional<ReadOnlyClubTrack> readClubTrack() throws DataLoadingException {
            return Optional.ofNullable(store.get(DEFAULT_PATH));
        }

        @Override
        public Optional<ReadOnlyClubTrack> readClubTrack(Path filePath) throws DataLoadingException {
            return Optional.ofNullable(store.get(filePath));
        }

        @Override
        public void saveClubTrack(ReadOnlyClubTrack addressBook) throws IOException {
            saved.put(DEFAULT_PATH, addressBook);
        }

        @Override
        public void saveClubTrack(ReadOnlyClubTrack addressBook, Path filePath) throws IOException {
            saved.put(filePath, addressBook);
        }

        // UserPrefsStorage methods
        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {

        }

        @Override
        public Path getUserPrefsFilePath() {
            return Paths.get("prefs.json");
        }

        @Override
        public Path getClubTrackFilePath() {
            return DEFAULT_PATH;
        }
    }
}
