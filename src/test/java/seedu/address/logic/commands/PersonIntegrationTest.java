package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Points;
import seedu.address.model.person.Tag;

/**
 * Integration tests to ensure Person model works correctly with all new fields.
 */
public class PersonIntegrationTest {

    @Test
    public void person_allFieldsPreserved_success() {
        // Create person with all new fields
        Name name = new Name("Test Person");
        Phone phone = new Phone("94351253");
        Email email = new Email("test@example.com");
        int yearOfStudy = 3;
        String faculty = "SOC";
        Address address = new Address("123 Test Street");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("President"));
        Boolean isPresent = true;
        Points points = new Points(100);

        Person person = new Person(name, phone, email, yearOfStudy, faculty,
            address, tags, isPresent, points);

        // Verify all fields are correctly stored
        assertEquals(name, person.getName());
        assertEquals(phone, person.getPhone());
        assertEquals(email, person.getEmail());
        assertEquals(yearOfStudy, person.getYearOfStudy());
        assertEquals(faculty, person.getFaculty());
        assertEquals(address, person.getAddress());
        assertEquals(tags, person.getTags());
        assertEquals(isPresent, person.isPresent());
        assertEquals(points, person.getPoints());
    }

    @Test
    public void person_pointsOperations_success() {
        Person person = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
            ALICE.getYearOfStudy(), ALICE.getFaculty(), ALICE.getAddress(),
            ALICE.getTags(), Boolean.FALSE, new Points(50));

        // Test points addition
        Points newPoints = person.getPoints().add(25);
        assertEquals(75, newPoints.getValue());
        assertEquals(50, person.getPoints().getValue()); // Original unchanged

        // Test points subtraction
        Points subtractedPoints = person.getPoints().subtract(30);
        assertEquals(20, subtractedPoints.getValue());
    }

    @Test
    public void addPoints_validPerson_success() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person person = model.getFilteredPersonList().get(0);

        AddPointsCommand command = new AddPointsCommand(Index.fromOneBased(1), 5);
        command.execute(model);

        assertEquals(person.getPoints().getValue() + 5,
            model.getFilteredPersonList().get(0).getPoints().getValue());
    }
}
