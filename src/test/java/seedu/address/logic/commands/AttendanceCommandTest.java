package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AttendanceCommandTest {

    @Test
    public void execute_attendanceCommand_listsPresentPersons() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Add persons with different attendance
        Person presentPerson = new PersonBuilder().withName("Alice").withPresent(true).build();
        Person absentPerson = new PersonBuilder().withName("Bob").withPresent(false).build();
        model.addPerson(presentPerson);
        model.addPerson(absentPerson);
        expectedModel.addPerson(presentPerson);
        expectedModel.addPerson(absentPerson);

        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_PRESENT_PERSONS);

        assertCommandSuccess(new AttendanceCommand(), model,
                AttendanceCommand.MESSAGE_SUCCESS, expectedModel);
    }
}

