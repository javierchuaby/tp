package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class AddPointsCommandParserTest {

    @Test
    public void parse_tooLargePoints_throwsParseException() {
        AddPointsCommandParser parser = new AddPointsCommandParser();
        // Test with points value that exceeds maximum allowed
        // Use a value larger than Integer.MAX_VALUE to trigger overflow handling
        assertThrows(ParseException.class, () -> parser.parse("1 pts/101"));
    }

}
