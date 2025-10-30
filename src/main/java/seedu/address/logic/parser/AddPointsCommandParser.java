package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPointsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINTS;

/**
 * Parses input arguments and creates a new AddPointsCommand object
 */
public class AddPointsCommandParser implements Parser<AddPointsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPointsCommand
     * and returns an AddPointsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPointsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPointsCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+");

        if (splitArgs.length != 2) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPointsCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(preamble);
            String pointsStr = argMultimap.getValue(PREFIX_POINTS).get();

            // parse as long to detect overflow / extremely large values
            long parsedLong = Long.parseLong(pointsStr);
            if (parsedLong > Integer.MAX_VALUE) {
                throw new ParseException("Too many points, please use a smaller value.");
            }
            int points = (int) parsedLong;

            if (points <= 0) {
                throw new ParseException("Points must be a positive integer");
            }

            if (points > Integer.MAX_VALUE / 2) {
                throw new ParseException("Points value too large");
            }

            return new AddPointsCommand(index, points);
        } catch (NumberFormatException nfe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPointsCommand.MESSAGE_USAGE));
        }
    }
}
