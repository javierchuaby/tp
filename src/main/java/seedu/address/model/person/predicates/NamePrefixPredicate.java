package seedu.address.model.person.predicates;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.model.person.Person;

/** Case-insensitive token-prefix match on {@code Person#getName()}. */
public final class NamePrefixPredicate implements Predicate<Person> {
    private final List<String> tokens; // lower-cased

    public NamePrefixPredicate(String query) {
        this.tokens = query == null || query.isBlank()
                ? List.of()
                : Arrays.stream(query.trim().split("\\s+"))
                .map(String::toLowerCase)
                .toList();
    }

    public NamePrefixPredicate(List<String> tokens) {
        this.tokens = tokens == null ? List.of()
                : tokens.stream().map(String::toLowerCase).toList();
    }

    @Override
    public boolean test(Person p) {
        if (tokens.isEmpty()) return true;
        String[] words = p.getName().fullName.toLowerCase().split("\\s+");
        return tokens.stream()
                .allMatch(t -> Arrays.stream(words).anyMatch(w -> w.startsWith(t)));
    }

    @Override public boolean equals(Object o) {
        return (o instanceof NamePrefixPredicate other) && tokens.equals(other.tokens);
    }

    @Override public int hashCode() { return tokens.hashCode(); }
}
