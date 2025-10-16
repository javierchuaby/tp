package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's points in the club.
 * Guarantees: point value is non-negative, immutable.
 */
public class Points {
    public static final String MESSAGE_CONSTRAINTS = "Points should be a non-negative integer";
    private final int value;

    /**
     * Constructs a Points object with the specified value.
     *
     * @param points A non-negative integer representing the points.
     * @throws IllegalArgumentException if points are negative.
     */
    public Points(int points) {
        requireNonNull(points);
        if (points < 0) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = points;
    }

    /**
     * Constructs a Points object starting with 0 points (for new members).
     */
    public Points() {
        this.value = 0;
    }

    /**
     * Returns a new Points object with one point added.
     */
    public Points addPoint() {
        return new Points(this.value + 1);
    }

    public Points subtract(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Delta must be non-negative");
        }
        return new Points(Math.max(0, this.value - delta));
    }

    public Points subtractPoint() {
        return subtract(1);
    }

    /**
     * Returns the point value.
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Points)) {
            return false;
        }

        Points otherPoints = (Points) other;
        return value == otherPoints.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
