package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    /**
     * Maximum lengths and corresponding error messages for selected fields.
     * These caps harden the parser against excessively long inputs during PE/testing.
     * Keep caps here (ParserUtil) for now; move into value objects post-freeze.
     */
    public static final int NAME_MAX_LEN = 100;
    public static final int ADDRESS_MAX_LEN = 200;
    public static final int FACULTY_MAX_LEN = 100;
    public static final int EMAIL_MAX_LEN = 254;

    /**
     * Error messages corresponding to the length caps above. Kept here so
     * all ParserUtil validations surface consistent, user-facing messages.
     */
    public static final String MESSAGE_NAME_TOO_LONG = "Name is too long (max 100 characters).";
    public static final String MESSAGE_ADDRESS_TOO_LONG = "Address is too long (max 200 characters).";
    public static final String MESSAGE_FACULTY_TOO_LONG = "Faculty is too long (max 100 characters).";
    public static final String MESSAGE_EMAIL_TOO_LONG = "Email is too long (max 254 characters).";

    /** Message used when an index string is not a non-zero unsigned integer. */
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    /** Message used when a year of study is missing/invalid (non-integer or out of allowed range). */
    public static final String MESSAGE_INVALID_YOS = "Year of study must be an integer between 1 and 5.";
    /** Message used when a faculty string is empty after trimming. */
    public static final String MESSAGE_INVALID_FACULTY = "Faculty cannot be empty.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the specified index is invalid (not a non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (trimmedName.length() > NAME_MAX_LEN) {
            throw new ParseException(MESSAGE_NAME_TOO_LONG);
        }
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (trimmedAddress.length() > ADDRESS_MAX_LEN) {
            throw new ParseException(MESSAGE_ADDRESS_TOO_LONG);
        }
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (trimmedEmail.length() > EMAIL_MAX_LEN) {
            throw new ParseException(MESSAGE_EMAIL_TOO_LONG);
        }
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String yearOfStudy} into an {@code int}. Trims whitespace.
     * Valid values are integers in {@code [1, 5]}.
     *
     * @param yearOfStudy raw user input (e.g., "2").
     * @return parsed year as {@code int}.
     * @throws ParseException if the input is not an integer in {@code [1, 5]}.
     */
    public static int parseYearOfStudy(String yearOfStudy) throws ParseException {
        requireNonNull(yearOfStudy);
        String trimmedYear = yearOfStudy.trim();
        try {
            int year = Integer.parseInt(trimmedYear);
            if (year < 1 || year > 5) {
                throw new ParseException(MESSAGE_INVALID_YOS);
            }
            return year;
        } catch (NumberFormatException nfe) {
            throw new ParseException(MESSAGE_INVALID_YOS);
        }
    }

    /**
     * Parses a {@code String faculty} into a trimmed {@code String}. Trims whitespace.
     * Enforces non-empty and a maximum length of {@value #FACULTY_MAX_LEN}.
     *
     * @param faculty raw user input.
     * @return trimmed faculty.
     * @throws ParseException if empty after trimming or length exceeds the cap.
     */
    public static String parseFaculty(String faculty) throws ParseException {
        requireNonNull(faculty);
        String trimmedFaculty = faculty.trim();
        if (trimmedFaculty.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FACULTY);
        }
        if (trimmedFaculty.length() > FACULTY_MAX_LEN) {
            throw new ParseException(MESSAGE_FACULTY_TOO_LONG);
        }
        return trimmedFaculty;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     *
     * @throws ParseException if any tag in {@code tags} is invalid.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
