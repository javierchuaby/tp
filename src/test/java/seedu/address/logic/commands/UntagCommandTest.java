package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;
import seedu.address.testutil.PersonBuilder;

import java.util.HashSet;

public class UntagCommandTest {
    @Test
    public void execute_untagPerson_success() {
        Model model = new ModelManager();
        Person person = new PersonBuilder().withTags("friend").build();
        model.addPerson(person);
        Tag tag = new Tag("friend");
        UntagCommand command = new UntagCommand(0, tag);

        // Prepare expected model
        Model expectedModel = new ModelManager();
        Person expectedPerson = new PersonBuilder().withTags("friend").build();
        expectedModel.addPerson(expectedPerson);
        expectedModel.setPerson(expectedPerson, new Person(
            expectedPerson.getName(),
            expectedPerson.getPhone(),
            expectedPerson.getEmail(),
            expectedPerson.getAddress(),
            new HashSet<>(),
            expectedPerson.isPresent()
        ));
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, tag);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertFalse(model.getFilteredPersonList().get(0).getTags().contains(tag));
    }
}
