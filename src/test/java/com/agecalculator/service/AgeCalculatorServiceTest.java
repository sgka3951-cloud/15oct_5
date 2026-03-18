package com.agecalculator.service;

import com.agecalculator.model.AgeResult;
import com.agecalculator.service.AgeCalculatorService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

/**
 * Comprehensive unit tests for {@link AgeCalculatorService}.
 *
 * <p>This test class verifies the core age calculation logic of the
 * {@link AgeCalculatorService#calculateAge(LocalDate, LocalDate)} method
 * across a variety of scenarios including normal dates, leap year dates,
 * edge cases (same-day birth, newborn), birthday boundaries, and
 * multi-decade spans.</p>
 *
 * <p><strong>Design principle:</strong> All test methods use deterministic
 * fixed {@link LocalDate} values created via {@link LocalDate#of(int, int, int)}.
 * No {@link LocalDate#now()} calls are used anywhere in this class, ensuring
 * that tests produce identical results regardless of the system clock.</p>
 *
 * <p>Since {@link AgeCalculatorService} accepts {@link LocalDate} parameters
 * directly (rather than calling {@code LocalDate.now()} internally), no
 * mocking is required — all test dates are injected as method arguments.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see AgeCalculatorService
 * @see AgeResult
 */
class AgeCalculatorServiceTest {

    /**
     * The service under test — a stateless instance shared across all test
     * methods. Since {@link AgeCalculatorService} contains no mutable state,
     * a single instance is safe for reuse without cross-test interference.
     */
    private final AgeCalculatorService service = new AgeCalculatorService();

    /**
     * Verifies correct age calculation for a normal (non-leap-year) date of
     * birth. Uses the AAP-specified example DOB of 15/08/1998.
     *
     * <p>Calculation breakdown:</p>
     * <ul>
     *   <li>DOB: 1998-08-15</li>
     *   <li>Current date: 2025-03-01</li>
     *   <li>Full years: 26 (Aug 15 1998 → Aug 15 2024)</li>
     *   <li>Remaining months: 6 (Aug 15 2024 → Feb 15 2025)</li>
     *   <li>Remaining days: 14 (Feb 15 2025 → Mar 1 2025 = 14 days in Feb 2025)</li>
     * </ul>
     */
    @Test
    @DisplayName("Calculate age for normal DOB 15/08/1998")
    void testCalculateAgeNormalDob() {
        // Arrange: AAP-specified normal DOB with a fixed reference date
        LocalDate dob = LocalDate.of(1998, 8, 15);
        LocalDate currentDate = LocalDate.of(2025, 3, 1);

        // Act: invoke the service to compute the age
        AgeResult result = service.calculateAge(dob, currentDate);

        // Assert: verify the result is non-null and all components are correct
        assertNotNull(result, "AgeResult should not be null");
        assertEquals(26, result.getYears(), "Expected 26 complete years from Aug 15, 1998 to Mar 1, 2025");
        assertEquals(6, result.getMonths(), "Expected 6 remaining months from Aug 15 to Feb 15");
        assertEquals(14, result.getDays(), "Expected 14 remaining days from Feb 15 to Mar 1 in 2025");
    }

    /**
     * Verifies correct age calculation for a leap year date of birth
     * (February 29). Uses the AAP-specified example DOB of 29/02/2000.
     *
     * <p>This test validates that the {@code java.time.Period} API correctly
     * handles the transition from a leap year DOB to a non-leap year reference
     * date. The Period.between algorithm adjusts the day deficit using the
     * length of February in the end year (28 days in 2025), resulting in
     * exactly 0 remaining days.</p>
     *
     * <p>Calculation breakdown:</p>
     * <ul>
     *   <li>DOB: 2000-02-29</li>
     *   <li>Current date: 2025-03-01</li>
     *   <li>Full years: 25 (Feb 29 2000 → Feb 28 2025, adjusted for non-leap year)</li>
     *   <li>Remaining months: 0 (Feb 28 to Mar 1 is less than 1 full month)</li>
     *   <li>Remaining days: 1 (Feb 28 2025 → Mar 1 2025)</li>
     *   <li>Result: 25 years, 0 months, 1 day</li>
     * </ul>
     */
    @Test
    @DisplayName("Calculate age for leap year DOB 29/02/2000")
    void testCalculateAgeLeapYearDob() {
        // Arrange: leap year DOB with a non-leap-year reference date
        LocalDate dob = LocalDate.of(2000, 2, 29);
        LocalDate currentDate = LocalDate.of(2025, 3, 1);

        // Act: invoke the service to compute the age
        AgeResult result = service.calculateAge(dob, currentDate);

        // Assert: verify leap year DOB is handled correctly
        assertNotNull(result, "AgeResult should not be null for leap year DOB");
        assertEquals(25, result.getYears(), "Expected 25 complete years from Feb 29, 2000 to Mar 1, 2025");
        assertEquals(0, result.getMonths(), "Expected 0 remaining months after leap year adjustment");
        assertEquals(1, result.getDays(), "Expected 1 remaining day (Feb 28 to Mar 1) after leap year adjustment");
    }

    /**
     * Verifies that when the date of birth is identical to the current date,
     * the resulting age is exactly zero in all components.
     *
     * <p>This is a critical edge case — {@code Period.between(date, date)}
     * must return a zero-length period (0 years, 0 months, 0 days).</p>
     */
    @Test
    @DisplayName("Calculate age when DOB equals current date")
    void testCalculateAgeSameDayBirth() {
        // Arrange: both DOB and current date are the same fixed date
        LocalDate date = LocalDate.of(2025, 1, 15);

        // Act: invoke the service with identical dates
        AgeResult result = service.calculateAge(date, date);

        // Assert: all age components must be zero
        assertNotNull(result, "AgeResult should not be null for same-day birth");
        assertEquals(0, result.getYears(), "Expected 0 years when DOB equals current date");
        assertEquals(0, result.getMonths(), "Expected 0 months when DOB equals current date");
        assertEquals(0, result.getDays(), "Expected 0 days when DOB equals current date");
    }

    /**
     * Verifies age calculation for a newborn — someone born one day before
     * the reference date. This is distinct from the same-day test (Test 3)
     * because it confirms that a single day of age is correctly computed.
     *
     * <p>Calculation breakdown:</p>
     * <ul>
     *   <li>DOB: 2025-06-14</li>
     *   <li>Current date: 2025-06-15</li>
     *   <li>Expected: 0 years, 0 months, 1 day</li>
     * </ul>
     */
    @Test
    @DisplayName("Calculate age for newborn born one day ago")
    void testCalculateAgeNewborn() {
        // Arrange: DOB is one day before the current date
        LocalDate dob = LocalDate.of(2025, 6, 14);
        LocalDate currentDate = LocalDate.of(2025, 6, 15);

        // Act: invoke the service to compute the age
        AgeResult result = service.calculateAge(dob, currentDate);

        // Assert: age should be exactly 1 day
        assertNotNull(result, "AgeResult should not be null for newborn");
        assertEquals(0, result.getYears(), "Expected 0 years for newborn");
        assertEquals(0, result.getMonths(), "Expected 0 months for newborn");
        assertEquals(1, result.getDays(), "Expected 1 day for newborn born yesterday");
    }

    /**
     * Verifies that on the exact birthday, the year count increments with
     * zero remaining months and zero remaining days.
     *
     * <p>Calculation breakdown:</p>
     * <ul>
     *   <li>DOB: 1990-05-20</li>
     *   <li>Current date: 2025-05-20 (exact 35th birthday)</li>
     *   <li>Expected: 35 years, 0 months, 0 days</li>
     * </ul>
     */
    @Test
    @DisplayName("Calculate age exactly on birthday boundary")
    void testCalculateAgeOnBirthdayBoundary() {
        // Arrange: current date falls exactly on the birthday
        LocalDate dob = LocalDate.of(1990, 5, 20);
        LocalDate currentDate = LocalDate.of(2025, 5, 20);

        // Act: invoke the service to compute the age
        AgeResult result = service.calculateAge(dob, currentDate);

        // Assert: exact birthday means whole years with no remainder
        assertNotNull(result, "AgeResult should not be null on birthday boundary");
        assertEquals(35, result.getYears(), "Expected exactly 35 years on 35th birthday");
        assertEquals(0, result.getMonths(), "Expected 0 months on exact birthday");
        assertEquals(0, result.getDays(), "Expected 0 days on exact birthday");
    }

    /**
     * Verifies correct age calculation over a multi-decade span (75+ years).
     * This ensures the {@code Period.between()} calculation scales correctly
     * for large age values.
     *
     * <p>Calculation breakdown:</p>
     * <ul>
     *   <li>DOB: 1950-01-01</li>
     *   <li>Current date: 2025-07-15</li>
     *   <li>Full years: 75 (Jan 1 1950 → Jan 1 2025)</li>
     *   <li>Remaining months: 6 (Jan 1 2025 → Jul 1 2025)</li>
     *   <li>Remaining days: 14 (Jul 1 2025 → Jul 15 2025)</li>
     * </ul>
     */
    @Test
    @DisplayName("Calculate age for multi-decade span")
    void testCalculateAgeMultiDecade() {
        // Arrange: DOB from 1950 with a 2025 reference date
        LocalDate dob = LocalDate.of(1950, 1, 1);
        LocalDate currentDate = LocalDate.of(2025, 7, 15);

        // Act: invoke the service to compute the age
        AgeResult result = service.calculateAge(dob, currentDate);

        // Assert: verify large age values are computed correctly
        assertNotNull(result, "AgeResult should not be null for multi-decade calculation");
        assertEquals(75, result.getYears(), "Expected 75 complete years from Jan 1, 1950 to Jul 15, 2025");
        assertEquals(6, result.getMonths(), "Expected 6 remaining months from Jan 1 to Jul 1");
        assertEquals(14, result.getDays(), "Expected 14 remaining days from Jul 1 to Jul 15");
    }
}
