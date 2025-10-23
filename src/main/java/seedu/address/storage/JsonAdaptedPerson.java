package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Points;
import seedu.address.model.person.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final Integer yearOfStudy; // boxed to allow null check
    private final String faculty;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final Boolean isPresent; // boxed to allow null defaulting
    private final Integer points; // boxed to allow null defaulting

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email,
            @JsonProperty("yearOfStudy") Integer yearOfStudy,
            @JsonProperty("faculty") String faculty,
            @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("isPresent") Boolean isPresent,
            @JsonProperty("points") Integer points) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.isPresent = isPresent; // may be null; default applied in toModelType()
        this.points = points; // may be null; default applied in toModelType()
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        this.name = source.getName().fullName;
        this.phone = source.getPhone().value;
        this.email = source.getEmail().value;
        this.yearOfStudy = source.getYearOfStudy();
        this.faculty = source.getFaculty();
        this.address = source.getAddress().value;
        this.tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        this.isPresent = source.isPresent();
        this.points = source.getPoints().getValue();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person}.
     *
     * @throws IllegalValueException if any data constraints are violated.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        // Name
        if (name == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        // Phone
        if (phone == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        // Email
        if (email == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        // Year of Study
        if (yearOfStudy == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Year of Study"));
        }
        if (yearOfStudy < 1 || yearOfStudy > 4) {
            throw new IllegalValueException("Year of Study must be between 1 and 4");
        }
        final int modelYearOfStudy = yearOfStudy;

        // Faculty
        if (faculty == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Faculty"));
        }
        final String modelFaculty = faculty;
        // (Add a Faculty.isValidFaculty(...) check here if you later model it as a value object)

        // Address
        if (address == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        // Tags & Points
        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Points modelPoints = new Points(points == null ? 0 : points);
        final boolean modelIsPresent = isPresent != null && isPresent;

        // Build the Person using the full constructor
        return new Person(
                modelName,
                modelPhone,
                modelEmail,
                modelYearOfStudy,
                modelFaculty,
                modelAddress,
                modelTags,
                modelIsPresent,
                modelPoints
        );
    }
}
