package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tag;

/**
 * A utility class to help with building {@link EditPersonDescriptor} objects.
 */
public class EditPersonDescriptorBuilder {

    private final EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details.
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmail(person.getEmail());
        descriptor.setAddress(person.getAddress());
        descriptor.setYearOfStudy(person.getYearOfStudy());
        descriptor.setFaculty(person.getFaculty());
        descriptor.setTags(person.getTags());
    }

    /** Sets the {@code Name} of the {@code EditPersonDescriptor} being built. */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /** Sets the {@code Phone} of the {@code EditPersonDescriptor} being built. */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /** Sets the {@code Email} of the {@code EditPersonDescriptor} being built. */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /** Sets the {@code Address} of the {@code EditPersonDescriptor} being built. */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code yearOfStudy} of the {@code EditPersonDescriptor} being built.
     * Accepts a string (e.g. "1", "2", "3", "4") and validates via {@link ParserUtil}.
     */
    public EditPersonDescriptorBuilder withYearOfStudy(String yearOfStudy) {
        try {
            descriptor.setYearOfStudy(ParserUtil.parseYearOfStudy(yearOfStudy));
        } catch (ParseException e) {
            // In tests we expect valid constants; surface invalid test data clearly.
            throw new IllegalArgumentException("Invalid yearOfStudy in test data: " + yearOfStudy, e);
        }
        return this;
    }

    /**
     * Sets the {@code faculty} of the {@code EditPersonDescriptor} being built.
     * Validates non-empty via {@link ParserUtil}.
     */
    public EditPersonDescriptorBuilder withFaculty(String faculty) {
        try {
            descriptor.setFaculty(ParserUtil.parseFaculty(faculty));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid faculty in test data: " + faculty, e);
        }
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it on the descriptor being built.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
