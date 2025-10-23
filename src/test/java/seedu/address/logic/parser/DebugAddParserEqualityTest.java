package seedu.address.logic.parser;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class DebugAddParserEqualityTest {
    public static void main(String[] args) throws Exception {
        AddCommandParser parser = new AddCommandParser();
        String input = " n/Bob Choo p/22222222 e/bob@example.com a/Block 123, Bobby Street 3 t/friend";
        AddCommand actual = parser.parse(input);
        Person expectedPerson = new PersonBuilder(TypicalPersons.BOB).withTags("friend").build();
        AddCommand expected = new AddCommand(expectedPerson);
        System.out.println("Actual toAdd: " + actual);
        System.out.println("Expected toAdd: " + expected);
        System.out.println("equals: " + expected.equals(actual));
        System.out.println("expected.toAdd.equals(actual.toAdd): " + expected.getClass().getDeclaredMethod("toString").invoke(expected));
    }
}

