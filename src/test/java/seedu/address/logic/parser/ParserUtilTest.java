package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "88888888";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        String maxValuePlusOne = Long.toString(Integer.MAX_VALUE + 1);
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, () -> ParserUtil.parseIndex(maxValuePlusOne));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<seedu.address.model.person.Tag> actualTagSet =
                ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    //Helper for generating long strings
    private static String repeat(char c, int n) {
        char[] arr = new char[n];
        Arrays.fill(arr, c);
        return new String(arr);
    }

    @Test
    public void parseYearOfStudy_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseYearOfStudy(null));
    }

    @Test
    public void parseYearOfStudy_nonInteger_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseYearOfStudy("two"));
        assertThrows(ParseException.class, () -> ParserUtil.parseYearOfStudy("2.0"));
        assertThrows(ParseException.class, () -> ParserUtil.parseYearOfStudy("  1x  "));
    }

    @Test
    public void parseYearOfStudy_outOfRange_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseYearOfStudy("0")); // below min
        assertThrows(ParseException.class, () -> ParserUtil.parseYearOfStudy("6")); // above max
    }

    @Test
    public void parseYearOfStudy_validBounds_success() throws Exception {
        assertEquals(1, ParserUtil.parseYearOfStudy("1"));
        assertEquals(5, ParserUtil.parseYearOfStudy("5"));
        assertEquals(2, ParserUtil.parseYearOfStudy("  2  ")); // trims whitespace
    }

    @Test
    public void parseFaculty_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseFaculty(null));
    }

    @Test
    public void parseFaculty_emptyAfterTrim_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseFaculty("   "));
    }

    @Test
    public void parseFaculty_tooLong_throwsParseException() {
        String over = repeat('F', ParserUtil.FACULTY_MAX_LEN + 1);
        assertThrows(ParseException.class, ParserUtil.MESSAGE_FACULTY_TOO_LONG, ()
                -> ParserUtil.parseFaculty(over));
    }

    @Test
    public void parseFaculty_valid_success() throws Exception {
        assertEquals("SoC", ParserUtil.parseFaculty("SoC"));
        assertEquals("Business", ParserUtil.parseFaculty("  Business  "));
    }

    @Test
    public void parseName_tooLong_throwsParseException() {
        String over = repeat('a', ParserUtil.NAME_MAX_LEN + 1);
        assertThrows(ParseException.class, ParserUtil.MESSAGE_NAME_TOO_LONG, ()
                -> ParserUtil.parseName(over));
    }

    @Test
    public void parseName_boundaryAtMax_success() throws Exception {
        String atMax = repeat('a', ParserUtil.NAME_MAX_LEN);
        assertEquals(new Name(atMax), ParserUtil.parseName(atMax));
    }

    @Test
    public void parseAddress_tooLong_throwsParseException() {
        String over = repeat('A', ParserUtil.ADDRESS_MAX_LEN + 1);
        assertThrows(ParseException.class, ParserUtil.MESSAGE_ADDRESS_TOO_LONG, ()
                -> ParserUtil.parseAddress(over));
    }

    @Test
    public void parseAddress_boundaryAtMax_success() throws Exception {
        String atMax = repeat('B', ParserUtil.ADDRESS_MAX_LEN);
        assertEquals(new Address(atMax), ParserUtil.parseAddress(atMax));
    }

    @Test
    public void parseEmail_tooLong_throwsParseException() {
        // Build a syntactically valid email that exceeds 254 chars total
        String domain = "@e.com"; // 6 chars
        int localLen = ParserUtil.EMAIL_MAX_LEN - domain.length() + 1; // +1 to exceed cap
        String tooLongEmail = repeat('x', localLen) + domain; // valid form, but exceeds cap
        assertThrows(ParseException.class, ParserUtil.MESSAGE_EMAIL_TOO_LONG, ()
                -> ParserUtil.parseEmail(tooLongEmail));
    }

    @Test
    public void parseEmail_boundaryAtMax_success() throws Exception {
        String domain = "@e.com"; // 6 chars
        int localLen = ParserUtil.EMAIL_MAX_LEN - domain.length(); // exactly at cap
        String atMaxEmail = repeat('y', localLen) + domain; // syntactically valid and at cap
        assertEquals(new Email(atMaxEmail), ParserUtil.parseEmail(atMaxEmail));
    }
}
