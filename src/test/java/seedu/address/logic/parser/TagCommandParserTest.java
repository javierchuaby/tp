package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.TagCommand;
import seedu.address.model.person.Tag;

public class TagCommandParserTest {
    @Test
    public void parse_validArgs_returnsTagCommand() throws Exception {
        TagCommandParser parser = new TagCommandParser();
        TagCommand command = parser.parse("1 friend");
        assertEquals(new TagCommand(0, new Tag("friend")), command);
    }
}

