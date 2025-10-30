package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class TagsPredicateTest {

    @Test
    public void emptyNeeded_matchesAll() {
        TagsPredicate p = new TagsPredicate(null, false);
        assertTrue(p.test(new PersonBuilder().withTags().build()));
    }

    @Test
    public void andSemantics_allPrefixesMustMatch() {
        TagsPredicate p = new TagsPredicate(List.of("treasurer", "dance"), false);
        assertTrue(p.test(new PersonBuilder().withTags("treasurer", "dance").build()));
        assertFalse(p.test(new PersonBuilder().withTags("treasurer").build()));
    }

    @Test
    public void orSemantics_anyPrefixMatches() {
        TagsPredicate p = new TagsPredicate(List.of("dance", "logi"), true);
        assertTrue(p.test(new PersonBuilder().withTags("logistics").build()));
        assertTrue(p.test(new PersonBuilder().withTags("dance").build()));
        assertFalse(p.test(new PersonBuilder().withTags("other").build()));
    }

    @Test
    public void prefixMatching_caseInsensitive() {
        TagsPredicate p = new TagsPredicate(List.of("Treas"), false);
        assertTrue(p.test(new PersonBuilder().withTags("Treasurer").build()));
    }

    @Test
    public void equals_hashCode_behaviour() {
        TagsPredicate a = new TagsPredicate(List.of("one", "two"), false);
        TagsPredicate b = new TagsPredicate(List.of("one", "two"), false);
        TagsPredicate c = new TagsPredicate(Set.of("one"), true);

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertEquals(a.hashCode(), b.hashCode());
    }
}
