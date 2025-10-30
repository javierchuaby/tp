package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class NamePrefixPredicateTest {

    @Test
    public void constructor_nullOrBlank_matchesAll() {
        NamePrefixPredicate pNull = new NamePrefixPredicate((String) null);
        NamePrefixPredicate pBlank = new NamePrefixPredicate("   ");
        Person person = new PersonBuilder().withName("Alice Bob").build();

        assertTrue(pNull.test(person));
        assertTrue(pBlank.test(person));
    }

    @Test
    public void singleAndMultipleTokens_matchingBehavior() {
        Person person = new PersonBuilder().withName("Charlotte Oliveira").build();

        // single token prefix
        NamePrefixPredicate single = new NamePrefixPredicate("char");
        assertTrue(single.test(person));

        // multiple tokens where both match some words
        NamePrefixPredicate multi = new NamePrefixPredicate("char oli");
        assertTrue(multi.test(person));

        // tokens that do not simultaneously match
        NamePrefixPredicate fail = new NamePrefixPredicate("cha dav");
        assertFalse(fail.test(person));
    }

    @Test
    public void equalsAndHashCode_behavesCorrectly() {
        NamePrefixPredicate a = new NamePrefixPredicate("Alice Bob");
        NamePrefixPredicate b = new NamePrefixPredicate(List.of("alice", "bob"));
        NamePrefixPredicate c = new NamePrefixPredicate("Different");

        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertEquals(a.hashCode(), b.hashCode());
    }
}
