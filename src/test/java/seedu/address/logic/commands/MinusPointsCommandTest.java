package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Tests for {@link MinusPointsCommand} behavior when subtracting would go below zero.
 */
public class MinusPointsCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_subtractBelowZero_throwsCommandException() {
        // assume typical persons start with 0 points
        MinusPointsCommand cmd = new MinusPointsCommand(INDEX_FIRST_PERSON, 1);
        assertCommandFailure(cmd, model, "Unable to subtract below 0 points");
    }
}
