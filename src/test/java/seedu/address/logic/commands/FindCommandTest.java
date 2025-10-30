package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = "No members found! Please recheck your input keywords.\n"
            + "Examples:\n"
            + "  find Alice\n"
            + "  find Computing\n"
            + "  find Y2\n"
            + "  find bob@example.com";
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        // With AND semantics, the previous multi-keyword search that expected three different
        // people to match is no longer appropriate. Test a single-keyword search instead.
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_caseInsensitiveYearOfStudyKeywords_personFound() {
        // Assuming TypicalPersons contains at least 1 person with yearOfStudy==2
        NameContainsKeywordsPredicate predicateUpper = preparePredicate("Y2");
        FindCommand commandUpper = new FindCommand(predicateUpper);
        expectedModel.updateFilteredPersonList(predicateUpper);
        assertCommandSuccess(commandUpper, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
        expectedModel.updateFilteredPersonList(p -> true);

        NameContainsKeywordsPredicate predicateLower = preparePredicate("y2");
        FindCommand commandLower = new FindCommand(predicateLower);
        expectedModel.updateFilteredPersonList(predicateLower);
        assertCommandSuccess(commandLower, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void execute_invalidSpelling_noPersonFoundWithMessage() {
        String input = "YeaarTwo";
        String expectedMessage = "No members found! Please recheck your input keywords.\n"
            + "Examples:\n"
            + "  find Alice\n"
            + "  find Computing\n"
            + "  find Y2\n"
            + "  find bob@example.com";

        NameContainsKeywordsPredicate predicate = preparePredicate(input);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emailAndFacultySearch_personFound() {
        NameContainsKeywordsPredicate emailPredicate = preparePredicate("alice@example.com");
        FindCommand emailCommand = new FindCommand(emailPredicate);
        expectedModel.updateFilteredPersonList(emailPredicate);
        assertCommandSuccess(emailCommand, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
        expectedModel.updateFilteredPersonList(p -> true);

        NameContainsKeywordsPredicate facultyPredicate = preparePredicate("School of Computing");
        FindCommand facultyCommand = new FindCommand(facultyPredicate);
        expectedModel.updateFilteredPersonList(facultyPredicate);
        assertCommandSuccess(facultyCommand, model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()),
                expectedModel);
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}

