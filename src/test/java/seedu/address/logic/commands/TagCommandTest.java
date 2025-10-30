package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;

public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexAndTag_success() {
        Person personToTag = model.getFilteredPersonList().get(0);
        Tag tagToAdd = new Tag("newTag");
        TagCommand tagCommand = new TagCommand(0, tagToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToTag.getTags());
        expectedTags.add(tagToAdd);

        Person expectedPerson = new Person(
            personToTag.getName(),
            personToTag.getPhone(),
            personToTag.getEmail(),
            personToTag.getYearOfStudy(),
            personToTag.getFaculty(),
            personToTag.getAddress(),
            expectedTags,
            personToTag.isPresent(),
            personToTag.getPoints()
        );

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tagToAdd);

        ModelManager expectedModel = new ModelManager(model.getClubTrack(), new UserPrefs());
        expectedModel.setPerson(personToTag, expectedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        int outOfBoundIndex = model.getFilteredPersonList().size();
        Tag tagToAdd = new Tag("validTag");
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagToAdd);

        assertCommandFailure(tagCommand, model, "The person index provided is invalid");
    }

    @Test
    public void equals() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        TagCommand tagCommand1 = new TagCommand(0, tag1);
        TagCommand tagCommand2 = new TagCommand(0, tag1);
        TagCommand tagCommand3 = new TagCommand(1, tag1);
        TagCommand tagCommand4 = new TagCommand(0, tag2);

        // same object -> returns true
        assertTrue(tagCommand1.equals(tagCommand1));

        // same values -> returns true
        assertTrue(tagCommand1.equals(tagCommand2));

        // different index -> returns false
        assertFalse(tagCommand1.equals(tagCommand3));

        // different tag -> returns false
        assertFalse(tagCommand1.equals(tagCommand4));

        // null -> returns false
        assertFalse(tagCommand1.equals(null));
    }
}
