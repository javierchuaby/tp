package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.TagsPredicate;

/**
 * Parses input arguments and creates a new {@code SearchCommand} object.
 * <p>
 * Expected format:
 * <pre>
 *   search t/TAG_PREFIX [t/TAG_PREFIX]...
 * </pre>
 * Constraints:
 * <ul>
 *   <li>No free-text preamble is allowed.</li>
 *   <li>At least one of {@code n/} or {@code t/} must be present.</li>
 *   <li>{@code n/} may appear at most once.</li>
 *   <li>{@code any/} is valid only if at least one {@code t/} is supplied.</li>
 * </ul>
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    @Override
    public SearchCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // Disallow stray text before prefixes.
        if (!map.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        List<String> tagValues = map.getAllValues(PREFIX_TAG).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // Require at least one tag prefix.
        if (tagValues.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        Predicate<Person> predicate = new TagsPredicate(tagValues, false /* AND semantics */);

        return new SearchCommand(predicate);
    }
}
