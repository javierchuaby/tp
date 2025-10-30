package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ClubTrack;
import seedu.address.model.ReadOnlyClubTrack;

public class JsonClubTrackStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readClubTrack_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readClubTrack(null));
    }

    private java.util.Optional<ReadOnlyClubTrack> readClubTrack(String filePath) throws Exception {
        return new JsonClubTrackStorage(Paths.get(filePath)).readClubTrack(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readClubTrack("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readClubTrack("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonClubTrack_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readClubTrack("invalidPersonAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonClubTrack_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readClubTrack("invalidAndValidPersonAddressBook.json"));
    }

    @Test
    public void readAndSaveClubTrack_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        ClubTrack original = getTypicalAddressBook();
        JsonClubTrackStorage jsonAddressBookStorage = new JsonClubTrackStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveClubTrack(original, filePath);
        ReadOnlyClubTrack readBack = jsonAddressBookStorage.readClubTrack(filePath).get();
        assertEquals(original, new ClubTrack(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAddressBookStorage.saveClubTrack(original, filePath);
        readBack = jsonAddressBookStorage.readClubTrack(filePath).get();
        assertEquals(original, new ClubTrack(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveClubTrack(original); // file path not specified
        readBack = jsonAddressBookStorage.readClubTrack().get(); // file path not specified
        assertEquals(original, new ClubTrack(readBack));

    }

    @Test
    public void saveAddressBook_nullClubTrack_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveClubTrack(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveClubTrack(ReadOnlyClubTrack addressBook, String filePath) {
        try {
            new JsonClubTrackStorage(Paths.get(filePath))
                    .saveClubTrack(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveClubTrack_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveClubTrack(new ClubTrack(), null));
    }
}
