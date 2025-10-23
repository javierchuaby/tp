package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Unit tests for {@code SwitchCommand}.
 */
public class SwitchCommandTest {

    @Test
    public void execute_validListName_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        String listName = "friends";
        expectedModel.setAddressBookFilePath(Paths.get("data", listName + ".json"));

        assertCommandSuccess(new SwitchCommand(listName), model,
                String.format(SwitchCommand.MESSAGE_SUCCESS, listName), expectedModel);
    }

}


