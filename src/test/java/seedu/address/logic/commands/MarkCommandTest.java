package seedu.address.logic.commands;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    private seedu.address.model.Model model = new seedu.address.model.ModelManager(
            seedu.address.testutil.TypicalPersons.getTypicalAddressBook(),
            new seedu.address.model.UserPrefs()
    );

    @org.junit.jupiter.api.Test
    public void execute_validIndexUnfilteredList_success() {
        seedu.address.model.person.Person personToMark =
                model.getFilteredPersonList()
                        .get(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        seedu.address.logic.commands.MarkCommand markCommand =
                new seedu.address.logic.commands.MarkCommand(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON);

        String expectedMessage = String.format(
                seedu.address.logic.commands.MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                personToMark.getName()
        );

        seedu.address.model.ModelManager expectedModel =
                new seedu.address.model.ModelManager(model.getAddressBook(), new seedu.address.model.UserPrefs());
        java.util.Set<seedu.address.model.tag.Tag> updatedTags = new java.util.HashSet<>(personToMark.getTags());
        updatedTags.add(new seedu.address.model.tag.Tag("present"));
        seedu.address.model.person.Person markedPerson = new seedu.address.model.person.Person(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                updatedTags,
                true
        );
        expectedModel.setPerson(personToMark, markedPerson);

        seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess(
                markCommand, model, expectedMessage, expectedModel
        );
    }

    @org.junit.jupiter.api.Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        seedu.address.commons.core.index.Index outOfBoundIndex =
                seedu.address.commons.core.index.Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        seedu.address.logic.commands.MarkCommand markCommand =
                new seedu.address.logic.commands.MarkCommand(outOfBoundIndex);

        seedu.address.logic.commands.CommandTestUtil.assertCommandFailure(
                markCommand, model, seedu.address.logic.Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX
        );
    }

    @org.junit.jupiter.api.Test
    public void execute_validIndexFilteredList_success() {
        seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex(
                model, seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON
        );

        seedu.address.model.person.Person personToMark =
                model.getFilteredPersonList()
                        .get(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        seedu.address.logic.commands.MarkCommand markCommand =
                new seedu.address.logic.commands.MarkCommand(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON);

        String expectedMessage = String.format(
                seedu.address.logic.commands.MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                personToMark.getName()
        );

        seedu.address.model.Model expectedModel =
                new seedu.address.model.ModelManager(model.getAddressBook(), new seedu.address.model.UserPrefs());
        java.util.Set<seedu.address.model.tag.Tag> updatedTags = new java.util.HashSet<>(personToMark.getTags());
        updatedTags.add(new seedu.address.model.tag.Tag("present"));
        seedu.address.model.person.Person markedPerson = new seedu.address.model.person.Person(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                updatedTags,
                true
        );
        expectedModel.setPerson(personToMark, markedPerson);
        seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex(
                expectedModel, seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON
        );

        seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess(
                markCommand, model, expectedMessage, expectedModel
        );
    }

    @org.junit.jupiter.api.Test
    public void equals() {
        seedu.address.logic.commands.MarkCommand markFirstCommand =
                new seedu.address.logic.commands.MarkCommand(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON);
        seedu.address.logic.commands.MarkCommand markSecondCommand =
                new seedu.address.logic.commands.MarkCommand(seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        seedu.address.logic.commands.MarkCommand markFirstCommandCopy =
                new seedu.address.logic.commands.MarkCommand(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON);
        org.junit.jupiter.api.Assertions.assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(markFirstCommand.equals(null));

        // different person -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(markFirstCommand.equals(markSecondCommand));
    }

    @org.junit.jupiter.api.Test
    public void toStringMethod() {
        seedu.address.commons.core.index.Index targetIndex =
                seedu.address.commons.core.index.Index.fromOneBased(1);
        seedu.address.logic.commands.MarkCommand markCommand =
                new seedu.address.logic.commands.MarkCommand(targetIndex);
        String expected = seedu.address.logic.commands.MarkCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + "}";
        org.junit.jupiter.api.Assertions.assertEquals(expected, markCommand.toString());
    }
}
