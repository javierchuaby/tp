package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Unit tests for {@code RemoveCommand}.
 */
public class RemoveCommandTest {

    @Test
    public void execute_validListName_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        String listName = "friends";

        assertCommandSuccess(new RemoveCommand(listName), model,
                String.format(RemoveCommand.MESSAGE_SUCCESS, listName), expectedModel);
    }

}


