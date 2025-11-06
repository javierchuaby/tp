package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person (club member).
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final int yearOfStudy;
    private final String faculty;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Boolean isPresent; // null = unmarked, true = present, false = absent
    private final Points points;

    /**
     * Constructs a {@code Person} with {@code isPresent=null} (unmarked) and fresh {@code Points}.
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email,
                  int yearOfStudy, String faculty,
                  Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.address = address;
        this.tags.addAll(tags);
        this.isPresent = null; // unmarked by default
        this.points = new Points();
    }

    /**
     * Constructs a {@code Person} with explicit presence.
     * @param isPresent null = unmarked, true = present, false = absent
     */
    public Person(Name name, Phone phone, Email email,
                  int yearOfStudy, String faculty,
                  Address address, Set<Tag> tags,
                  Boolean isPresent) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.address = address;
        this.tags.addAll(tags);
        this.isPresent = isPresent;
        this.points = new Points();
    }

    /**
     * Constructs a {@code Person} with explicit presence and points.
     * @param isPresent null = unmarked, true = present, false = absent
     */
    public Person(Name name, Phone phone, Email email,
                  int yearOfStudy, String faculty,
                  Address address, Set<Tag> tags,
                  Boolean isPresent, Points points) {
        requireAllNonNull(name, phone, email, address, tags, points);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.address = address;
        this.tags.addAll(tags);
        this.isPresent = isPresent;
        this.points = points;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getFaculty() {
        return faculty;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns the presence status of this person.
     * @return null if unmarked, true if present, false if absent
     */
    public Boolean isPresent() {
        return isPresent;
    }

    /** Returns the immutable points associated with this person. */
    public Points getPoints() {
        return points;
    }

    /** Identity: same email OR same phone (names can duplicate). */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null
                && (otherPerson.getEmail().equals(getEmail())
                || otherPerson.getPhone().equals(getPhone()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person o = (Person) other;
        return name.equals(o.name)
                && phone.equals(o.phone)
                && email.equals(o.email)
                && yearOfStudy == o.yearOfStudy
                && Objects.equals(faculty, o.faculty)
                && address.equals(o.address)
                && tags.equals(o.tags)
                && Objects.equals(isPresent, o.isPresent)
                && points.equals(o.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, yearOfStudy, faculty, address, tags, isPresent, points);
    }

    @Override
    public String toString() {
        // Keep the toString output compatible with existing tests which expect only certain fields.
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("year of study", yearOfStudy)
                .add("faculty", faculty)
                .add("address", address)
                .add("tags", tags)
                .add("isPresent", isPresent)
                .add("points", points)
                .toString();
    }

}
