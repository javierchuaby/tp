package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private StatusBarFooter statusBarFooter;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    // FXML fields for the inline help overlay (defined in MainWindow.fxml)
    @FXML
    private StackPane helpOverlay;

    @FXML
    private Label helpOverlayContent;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);
        addTextInputControlEventFilter(menuItem, keyCombination);
    }

    /**
     * Adds an event filter to handle accelerators when focus is in TextInputControl.
     * This is a workaround for JDK bug: https://bugs.openjdk.java.net/browse/JDK-8131666
     */
    private void addTextInputControlEventFilter(MenuItem menuItem, KeyCombination keyCombination) {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (isTextInputWithMatchingKey(event, keyCombination)) {
                executeMenuItemAction(menuItem, event);
            }
        });
    }

    /**
     * Checks if the event is from a TextInputControl and matches the key combination.
     */
    private boolean isTextInputWithMatchingKey(KeyEvent event, KeyCombination keyCombination) {
        return event.getTarget() instanceof TextInputControl && keyCombination.match(event);
    }

    /**
     * Executes the menu item action and consumes the event.
     */
    private void executeMenuItemAction(MenuItem menuItem, KeyEvent event) {
        menuItem.getOnAction().handle(new ActionEvent());
        event.consume();
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        fillPersonListPanel();
        fillResultDisplay();
        fillStatusBarFooter();
        fillCommandBox();
    }

    /**
     * Initializes and fills the person list panel.
     */
    private void fillPersonListPanel() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
    }

    /**
     * Initializes and fills the result display.
     */
    private void fillResultDisplay() {
        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
    }

    /**
     * Initializes and fills the status bar footer.
     */
    private void fillStatusBarFooter() {
        statusBarFooter = new StatusBarFooter(logic.getClubTrackFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
    }

    /**
     * Initializes and fills the command box.
     */
    private void fillCommandBox() {
        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        setWindowPosition(guiSettings);
    }

    /**
     * Sets the window position based on guiSettings if coordinates are available.
     */
    private void setWindowPosition(GuiSettings guiSettings) {
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (isHelpWindowShowing()) {
            helpWindow.focus();
        } else {
            helpWindow.show();
        }
    }

    /**
     * Checks if the help window is currently showing.
     */
    private boolean isHelpWindowShowing() {
        return helpWindow.isShowing();
    }

    /**
     * Handler for closing the inline help overlay (button onAction="#handleCloseHelpOverlay").
     */
    @FXML
    private void handleCloseHelpOverlay() {
        if (helpOverlay != null) {
            helpOverlay.setVisible(false);
            helpOverlay.setPickOnBounds(false);
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = executeCommandAndGetResult(commandText);
            handleCommandResult(commandResult);
            updateStatusBar();
            return commandResult;
        } catch (CommandException | ParseException e) {
            handleCommandError(commandText, e);
            throw e;
        }
    }

    /**
     * Executes the command and returns the result.
     */
    private CommandResult executeCommandAndGetResult(String commandText) throws CommandException, ParseException {
        CommandResult commandResult = logic.execute(commandText);
        logger.info("Result: " + commandResult.getFeedbackToUser());
        resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
        return commandResult;
    }

    /**
     * Handles the command result by showing help or exiting if needed.
     */
    private void handleCommandResult(CommandResult commandResult) {
        if (commandResult.isShowHelp()) {
            handleHelp();
        }
        if (commandResult.isExit()) {
            handleExit();
        }
    }

    /**
     * Updates the status bar with the current file path.
     */
    private void updateStatusBar() {
        if (statusBarFooter != null) {
            statusBarFooter.setSaveLocation(logic.getClubTrackFilePath());
        }
    }

    /**
     * Handles command execution errors.
     */
    private void handleCommandError(String commandText, Exception e) {
        logger.info("An error occurred while executing command: " + commandText);
        resultDisplay.setFeedbackToUser(e.getMessage());
    }
}
