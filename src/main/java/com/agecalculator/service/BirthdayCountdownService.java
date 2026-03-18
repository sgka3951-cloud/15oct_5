package com.agecalculator.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Stateless service class that calculates the number of days remaining until
 * a user's next birthday.
 *
 * <p>This service uses {@link LocalDate} arithmetic and {@link ChronoUnit#DAYS}
 * to compute the countdown from a given reference date to the next occurrence
 * of the user's birthday. It follows the Service Pattern — a stateless class
 * with a pure function that accepts input parameters and returns a result.</p>
 *
 * <p>All date parameters are injected via method arguments rather than calling
 * {@code LocalDate.now()} internally. This design enables dependency injection
 * readiness and straightforward unit testing with deterministic date values.</p>
 *
 * <p><strong>Leap year birthday handling:</strong> If the user's date of birth
 * is February 29 and the target year is not a leap year, the service gracefully
 * falls back to February 28 of that year for the birthday calculation. This
 * ensures the service never throws an exception for leap year birthdays in
 * non-leap years.</p>
 *
 * @see java.time.LocalDate
 * @see java.time.temporal.ChronoUnit
 */
public class BirthdayCountdownService {

    /**
     * Calculates the number of days remaining until the user's next birthday.
     *
     * <p>The method determines the user's birthday in the current year (based on
     * {@code currentDate}). If that birthday has already passed or falls on
     * {@code currentDate} itself, the countdown advances to the next year's
     * birthday. This guarantees the returned value always represents a future
     * date.</p>
     *
     * <p><strong>Leap year handling:</strong> When the date of birth is February 29
     * and the target year (current or next) is not a leap year, the birthday is
     * treated as February 28 of that year. This prevents {@code DateTimeException}
     * from being thrown when adjusting the year of a Feb-29 date.</p>
     *
     * @param dob         the user's date of birth; must not be {@code null}
     * @param currentDate the reference date from which to calculate the countdown,
     *                    typically today's date; must not be {@code null}
     * @return the number of days until the next birthday as a positive {@code long}
     *         value. If today is the birthday, returns the days until the next
     *         year's birthday (always a strictly positive value for valid inputs).
     */
    public long daysUntilNextBirthday(LocalDate dob, LocalDate currentDate) {
        // Step 1: Determine the user's birthday in the current year.
        // For leap-year birthdays (Feb 29) in non-leap years, fall back to Feb 28.
        LocalDate nextBirthday = resolveBirthdayForYear(dob, currentDate.getYear());

        // Step 2: If the birthday in the current year has already passed or is today,
        // advance to the next year's birthday so the countdown always targets a future date.
        if (nextBirthday.isBefore(currentDate) || nextBirthday.isEqual(currentDate)) {
            nextBirthday = resolveBirthdayForYear(dob, currentDate.getYear() + 1);
        }

        // Step 3: Calculate and return the number of days between currentDate and
        // the next birthday. The order of parameters is critical — currentDate first,
        // nextBirthday second — to produce a positive value.
        return ChronoUnit.DAYS.between(currentDate, nextBirthday);
    }

    /**
     * Resolves the birthday date for a given target year, handling the leap year
     * edge case where the date of birth is February 29 and the target year is
     * not a leap year.
     *
     * @param dob        the user's date of birth
     * @param targetYear the year in which to place the birthday
     * @return the birthday {@link LocalDate} in the target year, falling back to
     *         February 28 if the DOB is February 29 and the target year is not
     *         a leap year
     */
    private LocalDate resolveBirthdayForYear(LocalDate dob, int targetYear) {
        // Check if the DOB is a leap-year birthday (February 29)
        if (dob.getMonthValue() == 2 && dob.getDayOfMonth() == 29) {
            // If the target year is not a leap year, Feb 29 does not exist.
            // Fall back to Feb 28 to avoid DateTimeException.
            if (!LocalDate.of(targetYear, 1, 1).isLeapYear()) {
                return LocalDate.of(targetYear, 2, 28);
            }
        }
        // For all other dates (including Feb 29 in leap years), simply adjust the year.
        return dob.withYear(targetYear);
    }
}
