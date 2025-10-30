package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;

/**
 * The manager of the UI component.
 */
public class UiManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/address_book_32.png";

    private Logic logic;
    private MainWindow mainWindow;

    /**
     * Creates a {@code UiManager} with the given {@code Logic}.
     */
    public UiManager(Logic logic) {
        this.logic = logic;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");

        configurePrimaryStage(primaryStage);
        initializeMainWindow(primaryStage);
    }

    /**
     * Configures the primary stage with icon.
     */
    private void configurePrimaryStage(Stage primaryStage) {
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));
    }

    /**
     * Initializes and shows the main window.
     */
    private void initializeMainWindow(Stage primaryStage) {
        try {
            mainWindow = new MainWindow(primaryStage, logic);
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        Stage owner = (mainWindow == null) ? null : mainWindow.getPrimaryStage();
        showAlertDialogAndWait(owner, type, title, headerText, contentText);
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = createAlert(type, title, headerText, contentText);
        configureAlert(alert, owner);
        alert.showAndWait();
    }

    /**
     * Creates an alert with the given parameters.
     */
    private static Alert createAlert(AlertType type, String title, String headerText, String contentText) {
        final Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

    /**
     * Configures the alert dialog pane with styles and owner.
     */
    private static void configureAlert(Alert alert, Stage owner) {
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logError(title, e);
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        shutdownApplication();
    }

    /**
     * Logs the error with details.
     */
    private void logError(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
    }

    /**
     * Shuts down the application.
     */
    private void shutdownApplication() {
        Platform.exit();
        System.exit(1);
    }

}
