package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Points;

public class AddPointsCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexAndPoints_success() {
        Person personToUpdate = model.getFilteredPersonList().get(0);
        Index index = Index.fromZeroBased(0);
        int pointsToAdd = 50;
        AddPointsCommand addPointsCommand = new AddPointsCommand(index, pointsToAdd);

        Points newPoints = personToUpdate.getPoints().add(pointsToAdd);
        Person expectedPerson = new Person(
            personToUpdate.getName(),
            personToUpdate.getPhone(),
            personToUpdate.getEmail(),
            personToUpdate.getYearOfStudy(),
            personToUpdate.getFaculty(),
            personToUpdate.getAddress(),
            personToUpdate.getTags(),
            personToUpdate.isPresent(),
            newPoints
        );

        String expectedMessage = String.format(AddPointsCommand.MESSAGE_SUCCESS,
            personToUpdate.getName(), pointsToAdd, newPoints.getValue());

        ModelManager expectedModel = new ModelManager(model.getClubTrack(), new UserPrefs());
        expectedModel.setPerson(personToUpdate, expectedPerson);

        assertCommandSuccess(addPointsCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddPointsCommand addPointsCommand = new AddPointsCommand(outOfBoundIndex, 10);

        assertCommandFailure(addPointsCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Index index1 = Index.fromZeroBased(0);
        Index index2 = Index.fromZeroBased(1);
        AddPointsCommand command1 = new AddPointsCommand(index1, 10);
        AddPointsCommand command2 = new AddPointsCommand(index1, 10);
        AddPointsCommand command3 = new AddPointsCommand(index2, 10);
        AddPointsCommand command4 = new AddPointsCommand(index1, 20);

        assertTrue(command1.equals(command1));
        assertTrue(command1.equals(command2));
        assertFalse(command1.equals(command3));
        assertFalse(command1.equals(command4));
        assertFalse(command1.equals(null));
    }
}
