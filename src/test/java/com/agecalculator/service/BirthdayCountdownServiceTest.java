package com.agecalculator.service;

import com.agecalculator.service.BirthdayCountdownService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link BirthdayCountdownService}.
 *
 * <p>This test class verifies the birthday countdown calculation logic implemented
 * in {@code BirthdayCountdownService.daysUntilNextBirthday(LocalDate dob, LocalDate currentDate)}.
 * All test methods use deterministic fixed {@link LocalDate} values — no
 * {@code LocalDate.now()} calls — ensuring repeatable results regardless of
 * the system clock.</p>
 *
 * <p>Test scenarios cover:</p>
 * <ul>
 *   <li>Basic countdown — birthday is the very next day (1 day away)</li>
 *   <li>Boundary condition — birthday falls on the current date (advances to next year)</li>
 *   <li>Mid-year countdown — birthday is approximately 6 months away</li>
 *   <li>Leap year handling — Feb 29 DOB in a non-leap year (falls back to Feb 28)</li>
 * </ul>
 *
 * <p><strong>Production class behavior:</strong> When the birthday is today or has
 * already passed in the current year, the service advances to the next year's birthday.
 * This means "birthday today" returns 365 or 366 days (not 0).</p>
 *
 * @see BirthdayCountdownService
 */
class BirthdayCountdownServiceTest {

    /**
     * Shared stateless service instance used across all test methods.
     * Since {@link BirthdayCountdownService} is stateless, a single instance is safe.
     */
    private final BirthdayCountdownService service = new BirthdayCountdownService();

    /**
     * Verifies the simplest countdown case: the user's birthday is exactly one day away.
     *
     * <p>Given a DOB of June 16 and a current date of June 15, 2025, the birthday
     * in 2025 has not yet occurred, so the countdown should return exactly 1 day.</p>
     */
    @Test
    @DisplayName("Days until next birthday when birthday is tomorrow")
    void testBirthdayTomorrow() {
        // Arrange: born June 16, current date is June 15 — birthday is tomorrow
        LocalDate dob = LocalDate.of(1990, 6, 16);
        LocalDate currentDate = LocalDate.of(2025, 6, 15);

        // Act: calculate days until next birthday
        long days = service.daysUntilNextBirthday(dob, currentDate);

        // Assert: birthday is exactly 1 day away
        assertEquals(1, days, "Birthday tomorrow should be exactly 1 day away");
    }

    /**
     * Verifies the boundary condition when the user's birthday is today.
     *
     * <p>The production {@link BirthdayCountdownService} advances to the next year's
     * birthday when the birthday is today (or has already passed). For a DOB of
     * March 15 and a current date of March 15, 2025, the countdown advances to
     * March 15, 2026. Since 2025–2026 does not include a February 29, the result
     * is 365 days.</p>
     */
    @Test
    @DisplayName("Days until next birthday when birthday is today")
    void testBirthdayToday() {
        // Arrange: born March 15, current date IS March 15 — birthday is today
        LocalDate dob = LocalDate.of(1990, 3, 15);
        LocalDate currentDate = LocalDate.of(2025, 3, 15);

        // Act: calculate days until next birthday
        long days = service.daysUntilNextBirthday(dob, currentDate);

        // Assert: birthday today advances to next year (2025 to 2026 = 365 days)
        assertTrue(days >= 0, "Days until next birthday must be non-negative");
        assertEquals(365, days,
                "Birthday today should advance to next year's birthday (365 days for 2025 to 2026)");
    }

    /**
     * Verifies a mid-year countdown where the birthday is approximately 6 months away.
     *
     * <p>Given a DOB of September 15 and a current date of March 15, 2025, the
     * birthday in 2025 has not yet passed. The countdown from March 15 to
     * September 15 covers:</p>
     * <ul>
     *   <li>March 16–31: 16 days</li>
     *   <li>April: 30 days</li>
     *   <li>May: 31 days</li>
     *   <li>June: 30 days</li>
     *   <li>July: 31 days</li>
     *   <li>August: 31 days</li>
     *   <li>September 1–15: 15 days</li>
     * </ul>
     * <p>Total: 16 + 30 + 31 + 30 + 31 + 31 + 15 = 184 days.</p>
     */
    @Test
    @DisplayName("Days until next birthday when birthday is approximately 6 months away")
    void testBirthdayInSixMonths() {
        // Arrange: born September 15, current date is March 15 — birthday is ~6 months away
        LocalDate dob = LocalDate.of(1990, 9, 15);
        LocalDate currentDate = LocalDate.of(2025, 3, 15);

        // Act: calculate days until next birthday
        long days = service.daysUntilNextBirthday(dob, currentDate);

        // Assert: March 15 to September 15, 2025 = 184 days
        assertEquals(184, days,
                "Birthday approximately 6 months away should be 184 days (March 15 to September 15)");
    }

    /**
     * Verifies correct handling of a Feb 29 (leap year) birthday in a non-leap year.
     *
     * <p>The production {@link BirthdayCountdownService} falls back to February 28
     * when the DOB is February 29 and the target year is not a leap year. For a
     * DOB of Feb 29, 2000 and a current date of January 15, 2025 (non-leap year),
     * the birthday resolves to February 28, 2025. The countdown covers:</p>
     * <ul>
     *   <li>January 16–31: 16 days</li>
     *   <li>February 1–28: 28 days</li>
     * </ul>
     * <p>Total: 16 + 28 = 44 days.</p>
     *
     * <p>This test is critical to ensure Feb 29 birthdays do not crash the
     * application in non-leap years.</p>
     */
    @Test
    @DisplayName("Days until next birthday for Feb 29 DOB in non-leap year")
    void testLeapYearBirthdayCountdown() {
        // Arrange: born Feb 29 (leap year), current date is Jan 15 in a non-leap year
        LocalDate dob = LocalDate.of(2000, 2, 29);
        LocalDate currentDate = LocalDate.of(2025, 1, 15);

        // Act: calculate days until next birthday
        long days = service.daysUntilNextBirthday(dob, currentDate);

        // Assert: Jan 15 to Feb 28, 2025 = 44 days (Feb 29 falls back to Feb 28)
        assertTrue(days > 0, "Days until leap year birthday in non-leap year must be positive");
        assertEquals(44, days,
                "Feb 29 DOB in non-leap year 2025 should fall back to Feb 28 (44 days from Jan 15)");
    }
}
