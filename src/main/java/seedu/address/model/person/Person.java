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
    private final boolean isPresent;
    private final Points points;

    /**
     * Constructs a {@code Person} with {@code isPresent=false} and fresh {@code Points}.
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
        this.isPresent = false;
        this.points = new Points();
    }

    /**
     * Constructs a {@code Person} with explicit presence.
     */
    public Person(Name name, Phone phone, Email email,
                  int yearOfStudy, String faculty,
                  Address address, Set<Tag> tags,
                  boolean isPresent) {
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
     */
    public Person(Name name, Phone phone, Email email,
                  int yearOfStudy, String faculty,
                  Address address, Set<Tag> tags,
                  boolean isPresent, Points points) {
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

    public boolean isPresent() {
        return isPresent;
    }

    /** Returns the immutable points associated with this person. */
    public Points getPoints() {
        return points;
    }

    /** Weaker identity: same name. */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null && otherPerson.getName().equals(getName());
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
                && isPresent == o.isPresent
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
                .toString();
    }

}
