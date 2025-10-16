package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.NamePrefixPredicate;
import seedu.address.model.person.predicates.TagsPredicate;

/**
 * Parses input arguments and creates a new {@code SearchCommand} object.
 * <p>
 * Expected format:
 * <pre>
 *   search [n/NAME_QUERY] [t/TAG]... [any/]
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
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG, PREFIX_ANY);

        // Disallow stray text before prefixes.
        if (!map.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        // Disallow duplicate name qualifiers.
        map.verifyNoDuplicatePrefixesFor(PREFIX_NAME);

        String nameQuery = map.getValue(PREFIX_NAME).orElse("").trim();

        List<String> tagValues = map.getAllValues(PREFIX_TAG).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        boolean anyTags = map.getValue(PREFIX_ANY).isPresent();

        // Require at least one filter.
        if (nameQuery.isEmpty() && tagValues.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        // any/ only makes sense with at least one tag.
        if (anyTags && tagValues.isEmpty()) {
            throw new ParseException("any/ requires at least one t/TAG.");
        }

        Predicate<Person> predicate =
                new NamePrefixPredicate(nameQuery)
                        .and(new TagsPredicate(tagValues, anyTags));

        return new SearchCommand(predicate);
    }
}
