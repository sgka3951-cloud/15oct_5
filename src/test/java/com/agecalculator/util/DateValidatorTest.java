package com.agecalculator.util;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.agecalculator.exception.FutureDateException;
import com.agecalculator.exception.InvalidDateFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive JUnit 5 unit tests for {@link DateValidator}.
 *
 * <p>This test class verifies the behaviour of the static method
 * {@link DateValidator#parseAndValidate(String)} across both positive and
 * negative scenarios:</p>
 * <ul>
 *   <li><strong>Valid date parsing:</strong> normal dates and leap-year dates are
 *       parsed correctly into {@link LocalDate} instances.</li>
 *   <li><strong>Invalid date rejection:</strong> impossible calendar dates
 *       (e.g.&nbsp;{@code "31/02/2020"}) throw
 *       {@link InvalidDateFormatException}.</li>
 *   <li><strong>Future date rejection:</strong> any date after
 *       {@link LocalDate#now()} throws {@link FutureDateException}.</li>
 *   <li><strong>Format validation:</strong> strings that do not conform to
 *       {@code DD/MM/YYYY} throw {@link InvalidDateFormatException}.</li>
 *   <li><strong>Edge cases:</strong> {@code null}, empty strings, and arbitrary
 *       non-date strings are all rejected with
 *       {@link InvalidDateFormatException}.</li>
 * </ul>
 *
 * <p>All tests call only {@code DateValidator.parseAndValidate(String)} — no
 * production logic is duplicated.  The class is stateless and requires no
 * fields or constructor.</p>
 *
 * @see DateValidator
 * @see FutureDateException
 * @see InvalidDateFormatException
 */
class DateValidatorTest {

    // -----------------------------------------------------------------------
    // Positive Tests — valid date parsing
    // -----------------------------------------------------------------------

    /**
     * Verifies that a normal, valid date string ({@code "15/08/1998"}) is
     * parsed into the expected {@link LocalDate} value.  This is the core use
     * case from AAP Section&nbsp;0.7.2.
     */
    @Test
    @DisplayName("Valid date '15/08/1998' is parsed correctly")
    void testValidDateIsParsedCorrectly() {
        // Arrange & Act
        LocalDate result = DateValidator.parseAndValidate("15/08/1998");

        // Assert
        assertNotNull(result, "Parsed date must not be null");
        assertEquals(LocalDate.of(1998, 8, 15), result,
                "Parsed date should match 1998-08-15");
    }

    /**
     * Verifies that a valid leap-year date ({@code "29/02/2000"}) is accepted
     * and parsed correctly.  February&nbsp;29 is only valid in leap years —
     * 2000 is a leap year.
     */
    @Test
    @DisplayName("Leap year date '29/02/2000' is parsed correctly")
    void testLeapYearDateIsParsedCorrectly() {
        // Arrange & Act
        LocalDate result = DateValidator.parseAndValidate("29/02/2000");

        // Assert
        assertNotNull(result, "Parsed leap-year date must not be null");
        assertEquals(LocalDate.of(2000, 2, 29), result,
                "Parsed date should match 2000-02-29");
    }

    // -----------------------------------------------------------------------
    // Negative Tests — invalid date rejection
    // -----------------------------------------------------------------------

    /**
     * Verifies that an impossible calendar date ({@code "31/02/2020"}) is
     * rejected with an {@link InvalidDateFormatException}.  February never
     * has 31&nbsp;days — the strict resolver catches this.
     */
    @Test
    @DisplayName("Invalid date '31/02/2020' throws InvalidDateFormatException")
    void testInvalidCalendarDateThrowsException() {
        InvalidDateFormatException ex = assertThrows(
                InvalidDateFormatException.class,
                () -> DateValidator.parseAndValidate("31/02/2020"),
                "Impossible calendar date should throw InvalidDateFormatException"
        );

        // Verify a meaningful message is provided
        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }

    /**
     * Verifies that a date in the future triggers a
     * {@link FutureDateException}.  The future date is computed dynamically
     * as tomorrow ({@code LocalDate.now().plusDays(1)}) so the test never
     * becomes stale.
     */
    @Test
    @DisplayName("Future date throws FutureDateException")
    void testFutureDateThrowsException() {
        // Arrange — build a future date string in DD/MM/YYYY format
        LocalDate futureDate = LocalDate.now().plusDays(1);
        String futureDateStr = String.format("%02d/%02d/%04d",
                futureDate.getDayOfMonth(),
                futureDate.getMonthValue(),
                futureDate.getYear());

        // Act & Assert
        FutureDateException ex = assertThrows(
                FutureDateException.class,
                () -> DateValidator.parseAndValidate(futureDateStr),
                "A date after today should throw FutureDateException"
        );

        // Verify a meaningful message is provided
        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }

    /**
     * Verifies that a date string in the wrong format ({@code "2020-08-15"},
     * which is ISO format instead of {@code DD/MM/YYYY}) is rejected with an
     * {@link InvalidDateFormatException}.
     */
    @Test
    @DisplayName("Wrong format '2020-08-15' throws InvalidDateFormatException")
    void testWrongFormatThrowsException() {
        InvalidDateFormatException ex = assertThrows(
                InvalidDateFormatException.class,
                () -> DateValidator.parseAndValidate("2020-08-15"),
                "ISO format date should throw InvalidDateFormatException"
        );

        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }

    /**
     * Verifies that an empty string is rejected with an
     * {@link InvalidDateFormatException}.
     */
    @Test
    @DisplayName("Empty string throws InvalidDateFormatException")
    void testEmptyStringThrowsException() {
        InvalidDateFormatException ex = assertThrows(
                InvalidDateFormatException.class,
                () -> DateValidator.parseAndValidate(""),
                "Empty string should throw InvalidDateFormatException"
        );

        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }

    /**
     * Verifies that {@code null} input is rejected with an
     * {@link InvalidDateFormatException}.
     */
    @Test
    @DisplayName("Null input throws InvalidDateFormatException")
    void testNullInputThrowsException() {
        InvalidDateFormatException ex = assertThrows(
                InvalidDateFormatException.class,
                () -> DateValidator.parseAndValidate(null),
                "Null input should throw InvalidDateFormatException"
        );

        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }

    /**
     * Verifies that the arbitrary non-date string {@code "hello"} is rejected
     * with an {@link InvalidDateFormatException}.
     */
    @Test
    @DisplayName("Non-date string 'hello' throws InvalidDateFormatException")
    void testNonDateStringThrowsException() {
        InvalidDateFormatException ex = assertThrows(
                InvalidDateFormatException.class,
                () -> DateValidator.parseAndValidate("hello"),
                "Non-date string 'hello' should throw InvalidDateFormatException"
        );

        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }

    /**
     * Verifies that the arbitrary non-date string {@code "abc"} is rejected
     * with an {@link InvalidDateFormatException}.  This provides additional
     * edge-case coverage alongside the {@code "hello"} test.
     */
    @Test
    @DisplayName("Non-date string 'abc' throws InvalidDateFormatException")
    void testAnotherNonDateStringThrowsException() {
        InvalidDateFormatException ex = assertThrows(
                InvalidDateFormatException.class,
                () -> DateValidator.parseAndValidate("abc"),
                "Non-date string 'abc' should throw InvalidDateFormatException"
        );

        assertNotNull(ex.getMessage(),
                "Exception message must not be null");
    }
}
