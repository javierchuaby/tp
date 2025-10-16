package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UntagCommand;
import seedu.address.model.person.Tag;

public class UntagCommandParserTest {
    @Test
    public void parse_validArgs_returnsUntagCommand() throws Exception {
        UntagCommandParser parser = new UntagCommandParser();
        UntagCommand command = parser.parse("1 friend");
        assertEquals(new UntagCommand(0, new Tag("friend")), command);
    }
}


