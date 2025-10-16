package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Filters members in ClubTrack by name and/or tags.
 *
 * <p>Usage:
 * <pre>
 * search [/n NAME_QUERY] [/t TAG]... [/any]
 * </pre>
 * <ul>
 *   <li><b>/n NAME_QUERY</b> — case-insensitive, token-prefix match on name (e.g., "char oli").</li>
 *   <li><b>/t TAG</b> — repeatable; matches tags. AND by default; add <b>/any</b> to OR across tags.</li>
 *   <li>At least one of /n or /t must be supplied (parser enforces).</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * search /n char
 * search /t treasurer /t committee
 * search /n david /t family
 * search /t dance /t logistics /any
 * </pre>
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters members by name and/or tags.\n"
            + "Parameters:\n"
            + "  [/n NAME_QUERY]   token-prefix match (case-insensitive)\n"
            + "  [/t TAG]...       repeatable; AND by default; add /any for OR\n"
            + "  [/any]            optional; makes /t behave as OR\n"
            + "Examples:\n"
            + "  search /n char\n"
            + "  search /t treasurer /t committee\n"
            + "  search /n david /t family\n"
            + "  search /t dance /t logistics /any";

    private static final String MESSAGE_RESULT = "%d member(s) found";

    private final Predicate<Person> predicate;

    /**
     * Creates a {@code SearchCommand} with the given filter {@code predicate}.
     * The parser constructs this predicate from the supplied /n, /t, and /any arguments.
     */
    public SearchCommand(Predicate<Person> predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int count = model.getFilteredPersonList().size();
        return new CommandResult(String.format(MESSAGE_RESULT, count));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SearchCommand
                && predicate.equals(((SearchCommand) other).predicate));
    }
}
