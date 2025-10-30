package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class AddPointsCommandParserTest {

    @Test
    public void parse_tooLargePoints_throwsParseException() {
        AddPointsCommandParser parser = new AddPointsCommandParser();
        // Test with points value that exceeds maximum allowed
        assertThrows(ParseException.class, () -> parser.parse("1 p/999999999"));
    }

}
