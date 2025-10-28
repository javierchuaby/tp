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
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS, personToMark.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person markedPerson = new Person(
            personToMark.getName(),
            personToMark.getPhone(),
            personToMark.getEmail(),
            personToMark.getYearOfStudy(),
            personToMark.getFaculty(),
            personToMark.getAddress(),
            personToMark.getTags(),
            true,
            personToMark.getPoints()
        );
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS, personToMark.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person markedPerson = new Person(
            personToMark.getName(),
            personToMark.getPhone(),
            personToMark.getEmail(),
            personToMark.getYearOfStudy(),
            personToMark.getFaculty(),
            personToMark.getAddress(),
            personToMark.getTags(),
            true,
            personToMark.getPoints()
        );
        expectedModel.setPerson(personToMark, markedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAndPersonHasPoints_preservesPoints() {
        Person personWithPoints = new PersonBuilder().withPoints(100).build();
        model.addPerson(personWithPoints);

        // Get the index of the person we just added
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        MarkCommand markCommand = new MarkCommand(lastPersonIndex);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
            personWithPoints.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person markedPerson = new PersonBuilder(personWithPoints).withPresent(true).build();
        expectedModel.setPerson(personWithPoints, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);

        // Verify points are preserved
        Person resultPerson = model.getFilteredPersonList().get(lastPersonIndex.getZeroBased());
        assertEquals(100, resultPerson.getPoints().getValue());
    }

    @Test
    public void equals() {
        MarkCommand markFirstCommand = new MarkCommand(INDEX_FIRST_PERSON);
        MarkCommand markSecondCommand = new MarkCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(INDEX_FIRST_PERSON);
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkCommand markCommand = new MarkCommand(targetIndex);
        String expected = MarkCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, markCommand.toString());
    }
}
