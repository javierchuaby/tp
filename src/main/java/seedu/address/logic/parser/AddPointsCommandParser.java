package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINTS;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPointsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddPointsCommand object
 */
public class AddPointsCommandParser implements Parser<AddPointsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPointsCommand
     * and returns an AddPointsCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPointsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_POINTS);
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty() || !argMultimap.getValue(PREFIX_POINTS).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPointsCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(preamble);
            String pointsStr = argMultimap.getValue(PREFIX_POINTS).get();

            long parsedLong = Long.parseLong(pointsStr);
            if (parsedLong > 100) {
                throw new ParseException("Too many points, please use a smaller value.");
            }
            int points = (int) parsedLong;

            if (points <= 0) {
                throw new ParseException("Points must be a positive integer");
            }

            return new AddPointsCommand(index, points);
        } catch (NumberFormatException nfe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPointsCommand.MESSAGE_USAGE));
        }
    }
}
