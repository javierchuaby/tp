package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PointsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a {@link PointsCommand}.
 */
public class PointsCommandParser implements Parser<PointsCommand> {
    @Override
    public PointsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        Index index = ParserUtil.parseIndex(args.trim());
        return new PointsCommand(index);
    }
}
