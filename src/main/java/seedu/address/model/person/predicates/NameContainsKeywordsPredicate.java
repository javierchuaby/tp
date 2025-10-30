package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s fields (except tags) contain any of the given keywords, case-insensitively.
 * Fields checked: name, phone, email, address, faculty, year of study.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName;
        String phone = person.getPhone().toString();
        String email = person.getEmail().toString();
        String address = person.getAddress().toString();
        String faculty = String.valueOf(person.getFaculty());
        String year = String.valueOf(person.getYearOfStudy());

        return keywords.stream().anyMatch(raw -> {
            String kw = raw == null ? "" : raw.trim().toLowerCase();
            if (kw.isEmpty()) {
                return false;
            }
            return name.toLowerCase().contains(kw)
                    || phone.toLowerCase().contains(kw)
                    || email.toLowerCase().contains(kw)
                    || address.toLowerCase().contains(kw)
                    || faculty.toLowerCase().contains(kw)
                    || year.toLowerCase().contains(kw);
        });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
