package seedu.address.model.person;

/**
 * Represents a Person's points in the club.
 * Guarantees: point value is non-negative, immutable.
 */
public class Points {
    public static final int MAX_POINTS = 1000000;
    public static final String MESSAGE_CONSTRAINTS = "Points should be a non-negative integer";
    private final int value;

    /**
     * Constructs a Points object with the specified value.
     *
     * @param points A non-negative integer representing the points.
     * @throws IllegalArgumentException if points are negative.
     */
    public Points(int points) {
        if (points < 0) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        if (points > MAX_POINTS) {
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
     * Returns a new Points with {@code delta} added.
     *
     * @param delta non-negative amount to add
     * @throws IllegalArgumentException if {@code delta} is negative
     */
    public Points add(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Delta must be non-negative");
        }
        return new Points(this.value + delta);
    }

    /**
     * Returns a new Points with {@code delta} subtracted; result is never negative.
     *
     * @param delta non-negative amount to subtract
     * @throws IllegalArgumentException if {@code delta} is negative
     */
    public Points subtract(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Delta must be non-negative");
        }
        return new Points(Math.max(0, this.value - delta));
    }

    /**
     * Returns the point value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns true if points value is within acceptable business rules
     */
    public static boolean isValidPointsValue(int value) {
        return value >= 0 && value <= MAX_POINTS;
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
