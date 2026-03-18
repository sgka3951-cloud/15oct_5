package com.agecalculator.service;

import com.agecalculator.model.AgeResult;
import java.time.LocalDate;
import java.time.Period;

/**
 * Stateless service class for calculating a user's exact age based on a date of birth.
 *
 * <p>This class implements the core business logic for the Age Calculator application.
 * It uses the {@link java.time.Period} API to compute the exact difference between
 * two {@link java.time.LocalDate} instances, yielding a result expressed in years,
 * months, and days.</p>
 *
 * <p>This service follows the <strong>Service Pattern</strong> — it is a stateless class
 * with pure functions that accept input parameters and return results. It contains no
 * fields, no mutable state, and no side effects. Methods accept {@link LocalDate}
 * parameters rather than calling {@link LocalDate#now()} internally, enabling
 * <strong>dependency injection readiness</strong> and trivial unit testing without
 * mocking static methods.</p>
 *
 * <p>The {@link java.time.Period} class natively handles leap years and variable month
 * lengths, so no additional calendar logic is required.</p>
 *
 * <p><strong>Usage example:</strong></p>
 * <pre>{@code
 * AgeCalculatorService service = new AgeCalculatorService();
 * LocalDate dob = LocalDate.of(1998, 8, 15);
 * LocalDate today = LocalDate.now();
 * AgeResult result = service.calculateAge(dob, today);
 * System.out.println(result); // "Your age is X years, Y months, and Z days."
 * }</pre>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see AgeResult
 * @see java.time.Period
 */
public class AgeCalculatorService {

    /**
     * Calculates the exact age based on a date of birth and a reference date.
     *
     * <p>This method computes the elapsed time between the given date of birth and
     * the reference date using {@link Period#between(LocalDate, LocalDate)}. The
     * resulting {@link Period} is decomposed into its year, month, and day components,
     * which are encapsulated in an {@link AgeResult} value object and returned.</p>
     *
     * <p>The {@code java.time.Period} class natively handles:</p>
     * <ul>
     *   <li>Leap years (e.g., February 29 dates are correctly processed)</li>
     *   <li>Variable month lengths (28, 29, 30, or 31 days)</li>
     *   <li>Boundary conditions (e.g., same-day birth, exact year/month transitions)</li>
     * </ul>
     *
     * <p><strong>Note:</strong> This method does not perform input validation. It does
     * not check for null parameters or future dates. Input validation is the responsibility
     * of {@code DateValidator}, and the caller ({@code App}) is expected to provide
     * pre-validated input.</p>
     *
     * @param dob         the user's date of birth
     * @param currentDate the reference date, typically today's date obtained via
     *                    {@link LocalDate#now()} by the caller
     * @return an {@link AgeResult} containing the years, months, and days components
     *         of the calculated age
     */
    public AgeResult calculateAge(LocalDate dob, LocalDate currentDate) {
        Period period = Period.between(dob, currentDate);

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return new AgeResult(years, months, days);
    }
}
