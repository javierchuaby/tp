package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Filters and lists members in ClubTrack by name and/or tags.
 * <p>
 * Command word: {@code search}
 * <p>
 * Usage:
 * <pre>
 *   search [n/NAME_QUERY] [t/TAG]... [any/]
 * </pre>
 * Rules:
 * <ul>
 *   <li>{@code n/NAME_QUERY}: Case-insensitive, token-prefix match on the member's name
 *       (e.g., {@code n/char oli} matches “Charlotte Oliveira”).</li>
 *   <li>{@code t/TAG}: Repeatable; matches members containing the specified tag(s).
 *       By default, all listed tags must be present (logical AND).</li>
 *   <li>{@code any/}: Optional flag that changes tag matching from AND to OR
 *       (member must contain <em>any</em> of the given tags).</li>
 *   <li>At least one of {@code n/} or {@code t/} must be supplied.</li>
 * </ul>
 * Examples:
 * <pre>
 *   search n/char
 *   search t/treasurer t/committee
 *   search n/david t/family
 *   search t/dance t/logistics any/
 * </pre>
 */
public class SearchCommand extends Command {

    /** Command word used to invoke this command. */
    public static final String COMMAND_WORD = "search";

    /** Usage message shown when the input format is invalid. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters members by name and/or tags.\n"
            + "Parameters:\n"
            + "  [n/NAME_QUERY]   token-prefix match (case-insensitive)\n"
            + "  [t/TAG]...       repeatable; AND by default; add any/ for OR\n"
            + "  [any/]           optional; makes t/ behave as OR\n"
            + "Examples:\n"
            + "  search n/char\n"
            + "  search t/treasurer t/committee\n"
            + "  search n/david t/family\n"
            + "  search t/dance t/logistics any/";

    private static final String MESSAGE_RESULT = "%d member(s) found";

    private final Predicate<Person> predicate;

    /**
     * Constructs a {@code SearchCommand} with the given filtering {@code predicate}.
     *
     * @param predicate Predicate to filter the displayed member list. Must not be {@code null}.
     */
    public SearchCommand(Predicate<Person> predicate) {
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
