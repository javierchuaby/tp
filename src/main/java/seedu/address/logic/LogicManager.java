package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.RemoveCommand;
import seedu.address.logic.commands.SwitchCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
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

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);

        // Special handling for switch/remove commands because they affect which file is used on disk.
        if (command instanceof SwitchCommand) {
            SwitchCommand sc = (SwitchCommand) command;
            // execute to update model file path (SwitchCommand.execute also sets model path)
            commandResult = command.execute(model);
            Path filePath = sc.getFilePath();

            try {
                Optional<ReadOnlyAddressBook> data = storage.readAddressBook(filePath);
                if (data.isPresent()) {
                    model.setAddressBook(data.get());
                } else {
                    // create new empty file by saving an empty address book
                    model.setAddressBook(new AddressBook());
                    storage.saveAddressBook(model.getAddressBook(), filePath);
                }
                // Update the model's stored file path to reflect the switch
                model.setAddressBookFilePath(filePath);
            } catch (DataLoadingException dle) {
                // If loading failed, start with an empty address book but don't fail the command
                logger.warning("Failed to load list at " + filePath + ". Starting with empty list.");
                model.setAddressBook(new AddressBook());
                try {
                    storage.saveAddressBook(model.getAddressBook(), filePath);
                } catch (IOException ioe) {
                    throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
                }
                model.setAddressBookFilePath(filePath);
            } catch (IOException ioe) {
                throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
            }

            return commandResult;
        }

        if (command instanceof RemoveCommand) {
            RemoveCommand rc = (RemoveCommand) command;
            // run the command (returns confirmation message)
            commandResult = command.execute(model);
            Path filePath = rc.getFilePath();
            try {
                boolean deleted = Files.deleteIfExists(filePath);
                logger.fine("Attempted to delete " + filePath + ", deleted=" + deleted);
                // If the removed list was the currently loaded one, reset to default empty and file
                if (filePath.equals(model.getAddressBookFilePath())) {
                    Path defaultPath = Paths.get("data", "addressbook.json");
                    try {
                        Optional<ReadOnlyAddressBook> defaultData = storage.readAddressBook(defaultPath);
                        if (defaultData.isPresent()) {
                            // Load existing default address book without overwriting it
                            model.setAddressBook(defaultData.get());
                        } else {
                            // No default file exists: create an empty address book and save it
                            model.setAddressBook(new AddressBook());
                            storage.saveAddressBook(model.getAddressBook(), defaultPath);
                        }
                        model.setAddressBookFilePath(defaultPath);
                    } catch (DataLoadingException dle) {
                        // If default file exists but cannot be loaded (corrupted), do not overwrite it.
                        logger.warning("Default address book at " + defaultPath + " could not be loaded. "
                                + "Starting with an empty in-memory list without overwriting the file.");
                        model.setAddressBook(new AddressBook());
                        model.setAddressBookFilePath(defaultPath);
                    }
                }
            } catch (IOException ioe) {
                throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
            }

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
