package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

public class MinusPointsCommandParserTest {

    private final MinusPointsCommandParser parser = new MinusPointsCommandParser();

    @Test
    public void parse_tooLargePoints_throwsParseException() {
        String userInput = "1 pts/" + ("" + ((long) Integer.MAX_VALUE + 1));
        assertParseFailure(parser, userInput, "Too many points, please use a smaller value.");
    }

}
