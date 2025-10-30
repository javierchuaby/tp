package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Filters and lists members in ClubTrack by tags only.
 * <p>
 * Command word: {@code search}
 * <p>
 * Usage:
 * <pre>
 *   search t/TAG_PREFIX [t/TAG_PREFIX]...
 * </pre>
 * Rules:
 * <ul>
 *   <li>{@code t/TAG_PREFIX}: Repeatable; case-insensitive prefix on tag names.
 *       All provided prefixes must match at least one tag each (AND semantics).</li>
 *   <li>Only {@code t/} is allowed for this command.</li>
 * </ul>
 * Examples:
 * <pre>
 *   search t/tre
 *   search t/tre t/com
 * </pre>
 */
public class SearchCommand extends Command {

    /** Command word used to invoke this command. */
    public static final String COMMAND_WORD = "search";

    /** Usage message shown when the input format is invalid. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters members by tags only.\n"
            + "Parameters:\n"
            + " search t/ [tags]... (repeatable)\n"
            + "Correct Examples:\n"
            + "  search t/treasurer t/present\n"
            + "  search t/dance t/logistics";

    private static final String MESSAGE_RESULT = "%d member(s) found";

    private final Predicate<Person> predicate;

    /**
     * Constructs a {@code SearchCommand} with the given filtering {@code predicate}.
     *
     * @param predicate Predicate to filter the displayed member list. Must not be {@code null}.
     */
    public SearchCommand(Predicate<Person> predicate) {
        requireNonNull(predicate);
        this.predicate = requireNonNull(predicate);
    }

    /**
     * Executes the command by updating the model's filtered member list with the provided predicate.
     *
     * @param model The {@code Model} which the command operates on. Must not be {@code null}.
     * @return A {@code CommandResult} containing the number of members matched.
     */
    @Override
    public CommandResult execute(Model model) {
        assert predicate != null : "Predicate should not be null";
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
