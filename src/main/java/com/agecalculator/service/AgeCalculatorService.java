package com.agecalculator.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import com.agecalculator.model.AgeResult;

/**
 * Core service class that provides all age-related calculation functionality
 * for the Age Calculator application.
 *
 * <p>This class encapsulates the business logic for computing a person's age
 * from their date of birth. It offers four public methods:</p>
 * <ul>
 *   <li>{@link #calculateAge(LocalDate)} — exact age breakdown in years, months, and days</li>
 *   <li>{@link #getTotalMonths(LocalDate)} — total elapsed months since DOB</li>
 *   <li>{@link #getTotalDays(LocalDate)} — total elapsed days since DOB</li>
 *   <li>{@link #getDaysUntilNextBirthday(LocalDate)} — countdown in days to the next birthday</li>
 * </ul>
 *
 * <p>All methods accept a validated {@link LocalDate} representing the date of birth.
 * Input validation (format checking, future-date rejection) is handled upstream by
 * {@code DateValidator}; this class focuses exclusively on computation.</p>
 *
 * <p>Calculations use {@link java.time.Period} for date-based decomposition and
 * {@link java.time.temporal.ChronoUnit} for single-unit total calculations.
 * Both handle leap years and variable month lengths correctly.</p>
 *
 * <p>This class is stateless — it holds no instance fields and requires no
 * explicit constructor. A default no-argument constructor is sufficient.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class AgeCalculatorService {

    /**
     * Calculates the exact age based on the given date of birth.
     *
     * <p>Uses {@link Period#between(LocalDate, LocalDate)} to compute the elapsed
     * time between the DOB and the current system date, then decomposes the result
     * into its years, months, and days components. The returned {@link AgeResult}
     * encapsulates these three values as an immutable object.</p>
     *
     * <p>{@code Period.between()} correctly handles leap years, variable month
     * lengths, and all calendar edge cases (e.g., a DOB of Feb 29 in a leap year
     * computed against a non-leap-year current date).</p>
     *
     * @param dob the date of birth; must not be null or in the future
     *            (assumed already validated by {@code DateValidator})
     * @return an {@link AgeResult} containing the age breakdown in years, months,
     *         and days
     */
    public AgeResult calculateAge(LocalDate dob) {
        // Obtain the current system date as the reference endpoint
        LocalDate today = LocalDate.now();

        // Compute the elapsed period between DOB and today;
        // DOB must be the first argument to produce a positive period
        Period period = Period.between(dob, today);

        // Decompose the period into its year, month, and day components
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return new AgeResult(years, months, days);
    }

    /**
     * Calculates the total age in months as a single number.
     *
     * <p>Uses {@link ChronoUnit#MONTHS} to compute the total number of complete
     * months elapsed between the DOB and the current system date. Unlike
     * {@link #calculateAge(LocalDate)}, this returns a flat count rather than
     * a decomposed years/months/days breakdown.</p>
     *
     * @param dob the date of birth; must not be null or in the future
     *            (assumed already validated by {@code DateValidator})
     * @return the total number of elapsed months as a {@code long}
     */
    public long getTotalMonths(LocalDate dob) {
        return ChronoUnit.MONTHS.between(dob, LocalDate.now());
    }

    /**
     * Calculates the total age in days as a single number.
     *
     * <p>Uses {@link ChronoUnit#DAYS} to compute the total number of days
     * elapsed between the DOB and the current system date. This accounts for
     * leap years and varying month lengths automatically.</p>
     *
     * @param dob the date of birth; must not be null or in the future
     *            (assumed already validated by {@code DateValidator})
     * @return the total number of elapsed days as a {@code long}
     */
    public long getTotalDays(LocalDate dob) {
        return ChronoUnit.DAYS.between(dob, LocalDate.now());
    }

    /**
     * Calculates the number of days remaining until the next birthday.
     *
     * <p>The algorithm works as follows:</p>
     * <ol>
     *   <li>Set the birthday to the current year using {@link LocalDate#withYear(int)}</li>
     *   <li>If the adjusted birthday has already passed (or is today), advance to next year</li>
     *   <li>Compute the days between today and the next birthday using
     *       {@link ChronoUnit#DAYS}</li>
     * </ol>
     *
     * <p><strong>Leap year handling:</strong> When the DOB is February 29 (a leap
     * year birthday) and the target year is not a leap year,
     * {@link LocalDate#withYear(int)} automatically adjusts the day-of-month to
     * the last valid day of February (i.e., February 28). This means the countdown
     * targets Feb 28 in non-leap years and Feb 29 in leap years.</p>
     *
     * @param dob the date of birth; must not be null or in the future
     *            (assumed already validated by {@code DateValidator})
     * @return the number of days until the next birthday as a {@code long};
     *         always a positive value (1–366)
     */
    public long getDaysUntilNextBirthday(LocalDate dob) {
        LocalDate today = LocalDate.now();

        // Adjust the birthday to fall within the current year.
        // withYear() safely handles Feb 29 DOBs: in non-leap years it adjusts
        // to Feb 28, which is the expected behavior for birthday countdowns.
        LocalDate nextBirthday = dob.withYear(today.getYear());

        // If the birthday this year has already passed or is today,
        // advance to the next year's birthday
        if (!nextBirthday.isAfter(today)) {
            nextBirthday = dob.withYear(today.getYear() + 1);
        }

        // Compute and return the number of days from today to the next birthday
        return ChronoUnit.DAYS.between(today, nextBirthday);
    }
}
