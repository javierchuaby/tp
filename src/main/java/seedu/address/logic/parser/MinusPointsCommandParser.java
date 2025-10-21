package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MinusPointsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new MinusPointsCommand object
 */
public class MinusPointsCommandParser implements Parser<MinusPointsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MinusPointsCommand
     * and returns a MinusPointsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MinusPointsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] splitArgs = trimmedArgs.split("\\s+");

        if (splitArgs.length != 2) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MinusPointsCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(splitArgs[0]);
            int points = Integer.parseInt(splitArgs[1]);

            if (points <= 0) {
                throw new ParseException("Points must be a positive integer");
            }

            return new MinusPointsCommand(index, points);
        } catch (NumberFormatException nfe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MinusPointsCommand.MESSAGE_USAGE));
        }
    }
}
