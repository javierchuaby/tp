package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnmarkCommand.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUnmark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Ensure the person is currently present to test successful unmark
        Person presentVersion = new Person(
                personToUnmark.getName(),
                personToUnmark.getPhone(),
                personToUnmark.getEmail(),
                personToUnmark.getYearOfStudy(),
                personToUnmark.getFaculty(),
                personToUnmark.getAddress(),
                personToUnmark.getTags(),
                true,
                personToUnmark.getPoints());
        model.setPerson(personToUnmark, presentVersion);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON);

        Person unmarkedPerson = new Person(
                presentVersion.getName(),
                presentVersion.getPhone(),
                presentVersion.getEmail(),
                presentVersion.getYearOfStudy(),
                presentVersion.getFaculty(),
                presentVersion.getAddress(),
                presentVersion.getTags(),
                false,
                presentVersion.getPoints()
        );

        String expectedMessage = String.format(
                UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                presentVersion.getName()
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(presentVersion, unmarkedPerson);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToUnmark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Ensure present first
        Person presentVersion = new Person(
                personToUnmark.getName(),
                personToUnmark.getPhone(),
                personToUnmark.getEmail(),
                personToUnmark.getYearOfStudy(),
                personToUnmark.getFaculty(),
                personToUnmark.getAddress(),
                personToUnmark.getTags(),
                true,
                personToUnmark.getPoints());
        model.setPerson(personToUnmark, presentVersion);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON);

        Person unmarkedPerson = new Person(
                presentVersion.getName(),
                presentVersion.getPhone(),
                presentVersion.getEmail(),
                presentVersion.getYearOfStudy(),
                presentVersion.getFaculty(),
                presentVersion.getAddress(),
                presentVersion.getTags(),
                false,
                presentVersion.getPoints()
        );

        String expectedMessage = String.format(
                UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                presentVersion.getName()
        );

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(presentVersion, unmarkedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_presentPersonUnfilteredList_success() {
        // First create a present person with +1 point as might happen after marking present
        Person base = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPresent = new Person(
                base.getName(),
                base.getPhone(),
                base.getEmail(),
                base.getYearOfStudy(),
                base.getFaculty(),
                base.getAddress(),
                base.getTags(),
                true,
                base.getPoints().addPoint()
        );
        model.setPerson(base, markedPresent);

        // Now test unmarking them (points unchanged)
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON);

        Person unmarkedPerson = new Person(
                markedPresent.getName(),
                markedPresent.getPhone(),
                markedPresent.getEmail(),
                markedPresent.getYearOfStudy(),
                markedPresent.getFaculty(),
                markedPresent.getAddress(),
                markedPresent.getTags(),
                false,
                markedPresent.getPoints()
        );

        String expectedMessage = String.format(
                UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                markedPresent.getName()
        );

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(markedPresent, unmarkedPerson);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        UnmarkCommand unmarkFirstCommand = new UnmarkCommand(INDEX_FIRST_PERSON);
        UnmarkCommand unmarkSecondCommand = new UnmarkCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommand));

        // same values -> returns true
        UnmarkCommand unmarkFirstCommandCopy = new UnmarkCommand(INDEX_FIRST_PERSON);
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommandCopy));

        // different types -> returns false
        assertFalse(unmarkFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unmarkFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unmarkFirstCommand.equals(unmarkSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(targetIndex);
        String expected = UnmarkCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unmarkCommand.toString());
    }
}
