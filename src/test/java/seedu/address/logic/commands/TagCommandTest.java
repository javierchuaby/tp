package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;
import seedu.address.testutil.PersonBuilder;

public class TagCommandTest {
    @Test
    public void execute_tagPerson_success() {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        model.addPerson(person);
        Tag tag = new Tag("friend");
        TagCommand command = new TagCommand(0, tag);

        // Prepare expected model
        Model expectedModel = new ModelManager();
        Person expectedPerson = new PersonBuilder().build();
        expectedModel.addPerson(expectedPerson);
        expectedModel.setPerson(expectedPerson, new Person(
            expectedPerson.getName(),
            expectedPerson.getPhone(),
            expectedPerson.getEmail(),
            expectedPerson.getAddress(),
            new java.util.HashSet<>() {{ add(tag); }},
            expectedPerson.isPresent()
        ));
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tag);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredPersonList().get(0).getTags().contains(tag));
    }
}
