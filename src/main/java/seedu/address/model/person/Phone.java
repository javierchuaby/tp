package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's mobile phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    /**
     * Mobile numbers in Singapore are exactly 8 digits and begin with 8 or 9.
     * We store the normalized digits-only form.
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Mobile number must be exactly 8 digits and start with 8 or 9 (e.g., 91234567).";
    // 8 digits, first digit 8 or 9
    public static final String VALIDATION_REGEX = "^[89]\\d{7}$";

    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        String normalized = normalize(phone);
        checkArgument(isValidPhone(normalized), MESSAGE_CONSTRAINTS);
        value = normalized;
    }

    /**
     * Returns true if a given string is a valid mobile phone number.
     * Input is normalized (spaces and dashes removed) before validation.
     */
    public static boolean isValidPhone(String test) {
        requireNonNull(test);
        String normalized = normalize(test);
        return normalized.matches(VALIDATION_REGEX);
    }

    /**
     * Remove common separators so "9123-4567" or "9123 4567" still validate.
     */
    private static String normalize(String raw) {
        return raw.trim().replaceAll("[\\s-]", "");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Phone)) {
            return false;
        }
        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
