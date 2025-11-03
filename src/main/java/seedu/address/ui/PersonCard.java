package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML private HBox cardPane;
    @FXML private Label name;
    @FXML private Label id;
    @FXML private Label phone;
    @FXML private Label address;
    @FXML private Label email;
    @FXML private Label points;
    @FXML private Label meta;
    @FXML private FlowPane tags;
    @FXML private FlowPane presencePane;
    @FXML private Label presence;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        initializeBasicFields(displayedIndex);
        initializePointsField();
        initializeMetaField();
        initializePresenceField();
        initializeTags();
    }

    /**
     * Initializes basic person information fields.
     */
    private void initializeBasicFields(int displayedIndex) {
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
    }

    /**
     * Initializes the points field if it exists.
     */
    private void initializePointsField() {
        if (points != null) {
            points.setText("Points: " + person.getPoints().getValue());
        }
    }

    /**
     * Initializes the meta field (year of study and faculty) if it exists.
     */
    private void initializeMetaField() {
        if (meta != null) {
            String metaText = buildMetaText();
            if (metaText.isEmpty()) {
                hideMetaField();
            } else {
                showMetaField(metaText);
            }
        }
    }

    /**
     * Builds the text for the meta field from year of study and faculty.
     */
    private String buildMetaText() {
        StringBuilder metaText = new StringBuilder();
        int yearOfStudy = person.getYearOfStudy();
        String faculty = person.getFaculty();

        if (yearOfStudy > 0) {
            metaText.append("Y").append(yearOfStudy);
        }
        if (faculty != null && !faculty.isBlank()) {
            if (metaText.length() > 0) {
                metaText.append(" · ");
            }
            metaText.append(faculty);
        }
        return metaText.toString();
    }

    /**
     * Hides the meta field.
     */
    private void hideMetaField() {
        meta.setManaged(false);
        meta.setVisible(false);
    }

    /**
     * Shows the meta field with the given text.
     */
    private void showMetaField(String text) {
        meta.setText(text);
        meta.setManaged(true);
        meta.setVisible(true);
    }

    /**
     * Initializes the presence indicator; shows it if person.isPresent() is true.
     */
    private void initializePresenceField() {
        if (presence == null || presencePane == null) {
            return;
        }

        if (person.isPresent()) {
            presence.setText("Present");
            presence.getStyleClass().remove("absent");
            if (!presence.getStyleClass().contains("present")) {
                presence.getStyleClass().add("present");
            }
            presence.setManaged(true);
            presence.setVisible(true);
            presencePane.setManaged(true);
            presencePane.setVisible(true);
        } else {
            presence.setManaged(false);
            presence.setVisible(false);
            presencePane.setManaged(false);
            presencePane.setVisible(false);
        }
    }

    /**
     * Initializes the tags by creating label components for each tag.
     */
    private void initializeTags() {
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(this::addTag);
    }

    /**
     * Adds a single tag to the tags flow pane.
     */
    private void addTag(Tag tag) {
        Label tagLabel = new Label(tag.tagName);
        tagLabel.getStyleClass().add("chip");
        tags.getChildren().add(tagLabel);
    }
}
