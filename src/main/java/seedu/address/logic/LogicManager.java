package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.RemoveCommand;
import seedu.address.logic.commands.SwitchCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private final AddressBookListManager listManager;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
        listManager = new AddressBookListManager(storage);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);

        // Special handling for switch/remove commands because they affect which file is used on disk.
        if (command instanceof SwitchCommand) {
            SwitchCommand sc = (SwitchCommand) command;
            // execute to update model file path (SwitchCommand.execute may set model path)
            commandResult = command.execute(model);
            // Delegate file-level handling to the list manager
            listManager.switchToList(sc.getListName(), model);
            return commandResult;
        }

        if (command instanceof RemoveCommand) {
            RemoveCommand rc = (RemoveCommand) command;
            // run the command (returns confirmation message)
            commandResult = command.execute(model);
            // Delegate file-level handling to the list manager
            listManager.removeList(rc.getListName(), model);
            return commandResult;
        }

        // Default behaviour for regular commands: execute and persist to the model's configured file path
        commandResult = command.execute(model);

        try {
            // If model has an explicit file path set (from user prefs), save to that path.
            Path currentPath = model.getAddressBookFilePath();
            if (currentPath != null) {
                storage.saveAddressBook(model.getAddressBook(), currentPath);
            } else {
                storage.saveAddressBook(model.getAddressBook());
            }
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
