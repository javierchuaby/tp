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
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnmarkCommand.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUnmark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS, personToUnmark.getName());

        ModelManager expectedModel = new ModelManager(model.getClubTrack(), new UserPrefs());
        Person unmarkedPerson = new Person(
            personToUnmark.getName(),
            personToUnmark.getPhone(),
            personToUnmark.getEmail(),
            personToUnmark.getYearOfStudy(),
            personToUnmark.getFaculty(),
            personToUnmark.getAddress(),
            personToUnmark.getTags(),
            false,
            personToUnmark.getPoints()
        );
        expectedModel.setPerson(personToUnmark, unmarkedPerson);

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
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS, personToUnmark.getName());

        Model expectedModel = new ModelManager(model.getClubTrack(), new UserPrefs());
        Person unmarkedPerson = new Person(
            personToUnmark.getName(),
            personToUnmark.getPhone(),
            personToUnmark.getEmail(),
            personToUnmark.getYearOfStudy(),
            personToUnmark.getFaculty(),
            personToUnmark.getAddress(),
            personToUnmark.getTags(),
            false,
            personToUnmark.getPoints()
        );
        expectedModel.setPerson(personToUnmark, unmarkedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAndPersonHasPoints_preservesPoints() {
        Person personWithPoints = new PersonBuilder().withPoints(150).withPresent(true).build();
        model.addPerson(personWithPoints);

        // Get the index of the person we just added
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        UnmarkCommand unmarkCommand = new UnmarkCommand(lastPersonIndex);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
            personWithPoints.getName());

        Model expectedModel = new ModelManager(model.getClubTrack(), new UserPrefs());
        Person unmarkedPerson = new PersonBuilder(personWithPoints).withPresent(false).build();
        expectedModel.setPerson(personWithPoints, unmarkedPerson);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);

        // Verify points are preserved
        Person resultPerson = model.getFilteredPersonList().get(lastPersonIndex.getZeroBased());
        assertEquals(150, resultPerson.getPoints().getValue());
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
