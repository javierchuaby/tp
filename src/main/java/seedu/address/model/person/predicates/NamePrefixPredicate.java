package seedu.address.model.person.predicates;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.model.person.Person;

/**
 * Tests that a {@link Person}'s name matches a set of query tokens using
 * <b>case-insensitive token-prefix</b> matching.
 *
 * <p>A person matches if <em>every</em> query token is a prefix of <em>some</em> word
 * in the person's full name. If the token list is empty, this predicate returns {@code true}
 * (i.e., it does not filter anything).</p>
 *
 * <p>Examples:
 * <pre>
 *   tokens: ["char"]            → matches "Charlotte Oliveira"
 *   tokens: ["char", "oli"]     → matches "Charlotte Oliveira"
 *   tokens: ["cha", "dav"]      → does NOT match "Charlotte Oliveira"
 * </pre>
 * </p>
 *
 * <p><b>Implementation note:</b> Matching is performed by lower-casing both the tokens and the name,
 * splitting the name on one or more whitespace characters, and checking
 * {@code word.startsWith(token)} for each token against any word.</p>
 */
public final class NamePrefixPredicate implements Predicate<Person> {

    /** Lower-cased query tokens. An empty list means "match all". */
    private final List<String> tokens;

    /**
     * Constructs a {@code NamePrefixPredicate} from a raw query string.
     * The string is split on whitespace into tokens; tokens are lower-cased.
     * A {@code null} or blank string results in an empty token list.
     *
     * @param query Raw query string (may be {@code null} or blank).
     */
    public NamePrefixPredicate(String query) {
        this.tokens = query == null || query.isBlank()
                ? List.of()
                : Arrays.stream(query.trim().split("\\s+"))
                .map(String::toLowerCase)
                .toList();
    }

    /**
     * Constructs a {@code NamePrefixPredicate} from a list of tokens.
     * Tokens are lower-cased. A {@code null} list results in an empty token list.
     *
     * @param tokens List of query tokens (may be {@code null}).
     */
    public NamePrefixPredicate(List<String> tokens) {
        this.tokens = tokens == null ? List.of()
                : tokens.stream().map(String::toLowerCase).toList();
    }

    /**
     * Returns {@code true} if the given {@code Person}'s name satisfies the
     * case-insensitive token-prefix rule described in the class header.
     * If this predicate was constructed with no tokens, always returns {@code true}.
     *
     * @param person The person to test. Must not be {@code null}.
     * @return {@code true} if the person's name matches; {@code false} otherwise.
     */
    @Override
    public boolean test(Person person) {
        if (tokens.isEmpty()) {
            return true;
        }
        String[] words = person.getName().fullName.toLowerCase().split("\\s+");
        return tokens.stream()
                .allMatch(t -> Arrays.stream(words).anyMatch(w -> w.startsWith(t)));
    }

    /**
     * Returns {@code true} if both predicates contain the same sequence of tokens.
     *
     * @param other The object to compare with.
     * @return {@code true} if equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof NamePrefixPredicate otherPred
                && tokens.equals(otherPred.tokens));
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return Hash code of this predicate.
     */
    @Override
    public int hashCode() {
        return tokens.hashCode();
    }
}
