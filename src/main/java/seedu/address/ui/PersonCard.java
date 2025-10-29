package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

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

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);

        if (points != null) {
            points.setText("Points: " + person.getPoints().getValue());
        }

        String faculty = person.getFaculty();
        int yearOfStudy = person.getYearOfStudy();

        StringBuilder metaText = new StringBuilder();
        if (yearOfStudy > 0) {
            metaText.append("Y").append(yearOfStudy);
        }
        if (faculty != null && !faculty.isBlank()) {
            if (metaText.length() > 0) {
                metaText.append(" · ");
            }
            metaText.append(faculty);
        }

        if (meta != null) {
            if (metaText.length() == 0) {
                meta.setManaged(false);
                meta.setVisible(false);
            } else {
                meta.setText(metaText.toString());
                meta.setManaged(true);
                meta.setVisible(true);
            }
        }

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.getStyleClass().add("chip");
                    tags.getChildren().add(tagLabel);
                });
    }
}
