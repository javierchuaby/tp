package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;
import seedu.address.testutil.PersonBuilder;

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

        HashSet<Tag> updatedTags = new HashSet<>(expectedPerson.getTags());
        updatedTags.remove(tag);

        Person expectedUpdatedPerson = new Person(
                expectedPerson.getName(),
                expectedPerson.getPhone(),
                expectedPerson.getEmail(),
                expectedPerson.getYearOfStudy(),
                expectedPerson.getFaculty(),
                expectedPerson.getAddress(),
                updatedTags,
                expectedPerson.isPresent(),
                expectedPerson.getPoints()
        );

        expectedModel.setPerson(expectedPerson, expectedUpdatedPerson);

        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, tag);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertFalse(model.getFilteredPersonList().get(0).getTags().contains(tag));
    }
}
