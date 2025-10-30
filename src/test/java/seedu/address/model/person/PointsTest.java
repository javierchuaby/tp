package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PointsTest {

    @Test
    public void constructor_validPoints_success() {
        Points points = new Points(100);
        assertEquals(100, points.getValue());
    }

    @Test
    public void constructor_defaultConstructor_zeroPoints() {
        Points points = new Points();
        assertEquals(0, points.getValue());
    }

    @Test
    public void constructor_negativePoints_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Points(-1));
    }

    @Test
    public void constructor_exceedsMaxPoints_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Points(Points.MAX_POINTS + 1));
    }

    @Test
    public void add_validDelta_success() {
        Points points = new Points(100);
        Points result = points.add(50);
        assertEquals(150, result.getValue());
        assertEquals(100, points.getValue()); // original unchanged
    }

    @Test
    public void add_negativeDelta_throwsException() {
        Points points = new Points(100);
        assertThrows(IllegalArgumentException.class, () -> points.add(-1));
    }

    @Test
    public void subtract_validDelta_success() {
        Points points = new Points(100);
        Points result = points.subtract(30);
        assertEquals(70, result.getValue());
    }

    @Test
    public void subtract_exceedsValue_returnsZero() {
        Points points = new Points(50);
        Points result = points.subtract(100);
        assertEquals(0, result.getValue());
    }

    @Test
    public void isValidPointsValue() {
        assertTrue(Points.isValidPointsValue(0));
        assertTrue(Points.isValidPointsValue(100));
        assertTrue(Points.isValidPointsValue(Points.MAX_POINTS));
        assertFalse(Points.isValidPointsValue(-1));
        assertFalse(Points.isValidPointsValue(Points.MAX_POINTS + 1));
    }

    @Test
    public void equals() {
        Points points1 = new Points(100);
        Points points2 = new Points(100);
        Points points3 = new Points(200);

        assertTrue(points1.equals(points1));
        assertTrue(points1.equals(points2));
        assertFalse(points1.equals(points3));
        assertFalse(points1.equals(null));
        assertFalse(points1.equals("not points"));
    }

    @Test
    public void toString_test() {
        Points points = new Points(123);
        assertEquals("123", points.toString());
    }
}
