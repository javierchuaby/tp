package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class AttendanceCommandParserTest {

    private final AttendanceCommandParser parser = new AttendanceCommandParser();

    @Test
    public void parse_noArgs_returnsAttendanceCommand() throws Exception {
        assertTrue(parser.parse("").getClass() == AttendanceCommand.class);
    }

    @Test
    public void parse_withArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" extra"));
    }
}




