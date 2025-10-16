package seedu.address.model.person.predicates;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;

/** Matches on tags; AND by default, OR if {@code any=true}. */
public final class TagsPredicate implements Predicate<Person> {
    private final Set<String> needed; // lower-cased tag names
    private final boolean any;

    public TagsPredicate(Collection<String> tags, boolean any) {
        this.needed = tags == null ? Set.of()
                : tags.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.any = any;
    }

    @Override
    public boolean test(Person p) {
        if (needed.isEmpty()) return true;
        Set<String> has = p.getTags().stream()
                .map(t -> t.tagName.toLowerCase())
                .collect(Collectors.toSet());
        return any ? needed.stream().anyMatch(has::contains)
                : has.containsAll(needed);
    }

    @Override public boolean equals(Object o) {
        return (o instanceof TagsPredicate other)
                && any == other.any && needed.equals(other.needed);
    }

    @Override public int hashCode() { return needed.hashCode() * 31 + Boolean.hashCode(any); }
}
