package seedu.address.model.person.predicates;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;

/**
 * Tests whether a {@link Person} matches a required set of tag names.
 *
 * <p>Matching is <b>case-insensitive</b>. By default, a person must contain
 * <em>all</em> requested tags (logical AND). If {@code any} is {@code true},
 * a person matches if they contain <em>any</em> of the requested tags (logical OR).
 * If the requested tag set is empty, this predicate returns {@code true}
 * (i.e., it does not filter anything).</p>
 *
 * <p>Examples:
 * <pre>
 *   needed = {"treasurer"} (AND)            → matches anyone with "treasurer"
 *   needed = {"dance","logistics"} (AND)    → must have both tags
 *   needed = {"dance","logistics"} (OR)     → must have at least one
 * </pre>
 * </p>
 *
 * <p><b>Implementation note:</b> Both the required tag names and the person's tag names are
 * lower-cased prior to comparison. AND uses {@code has.containsAll(needed)}.
 * OR uses {@code needed.stream().anyMatch(has::contains)}.</p>
 */
public final class TagsPredicate implements Predicate<Person> {

    /** Lower-cased names of tags that must be present (all or any depending on {@link #any}). */
    private final Set<String> needed;

    /** If {@code true}, match any of the tags (OR); otherwise require all (AND). */
    private final boolean any;

    /**
     * Constructs a {@code TagsPredicate}.
     *
     * @param tags Collection of tag names to check against; may be {@code null}.
     *             When {@code null} or empty, this predicate matches all persons.
     * @param any  {@code true} to use OR semantics across the provided tags; {@code false} for AND.
     */
    public TagsPredicate(Collection<String> tags, boolean any) {
        this.needed = (tags == null)
                ? Set.of()
                : tags.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.any = any;
    }

    /**
     * Returns {@code true} if the given {@code Person}'s tags satisfy the rule
     * described in the class header (AND by default, OR if {@code any == true}).
     * If the required tag set is empty, always returns {@code true}.
     *
     * @param person The person to test. Must not be {@code null}.
     * @return {@code true} if the person's tags match; {@code false} otherwise.
     */
    @Override
    public boolean test(Person person) {
        if (needed.isEmpty()) {
            return true;
        }
        Set<String> has = person.getTags().stream()
                .map(t -> t.tagName.toLowerCase())
                .collect(Collectors.toSet());
        return any
                ? needed.stream().anyMatch(has::contains)
                : has.containsAll(needed);
    }

    /**
     * Returns {@code true} if both predicates have the same required tag set
     * and the same OR/AND semantics.
     *
     * @param other The object to compare with.
     * @return {@code true} if equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof TagsPredicate otherPred)
                && any == otherPred.any
                && needed.equals(otherPred.needed);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return Hash code of this predicate.
     */
    @Override
    public int hashCode() {
        return needed.hashCode() * 31 + Boolean.hashCode(any);
    }
}
