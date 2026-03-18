package com.agecalculator.model;

/**
 * Immutable data model that encapsulates the result of an age calculation.
 *
 * <p>This class holds the years, months, and days components of a person's
 * calculated age as determined by the difference between their date of birth
 * and the current system date. It follows the value object pattern — once
 * constructed, its state cannot be changed.</p>
 *
 * <p>Instances of this class are created and returned by
 * {@code AgeCalculatorService.calculateAge()} and consumed by
 * {@code AgeCalculatorApp} to display the formatted age result to the user.</p>
 *
 * <p>This class has zero dependencies on external libraries or other project
 * classes, making it the foundational class in the project's compilation order.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class AgeResult {

    /** The years component of the calculated age. */
    private final int years;

    /** The months component of the calculated age. */
    private final int months;

    /** The days component of the calculated age. */
    private final int days;

    /**
     * Constructs a new {@code AgeResult} with the specified years, months, and days.
     *
     * <p>The values provided should represent the decomposed components of an age
     * as computed by {@code java.time.Period.between()}, which always produces
     * valid non-negative values when the date of birth precedes the current date.</p>
     *
     * @param years  the number of complete years in the age
     * @param months the number of remaining complete months beyond the years
     * @param days   the number of remaining days beyond the months
     */
    public AgeResult(int years, int months, int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }

    /**
     * Returns the years component of the calculated age.
     *
     * @return the number of complete years
     */
    public int getYears() {
        return years;
    }

    /**
     * Returns the months component of the calculated age.
     *
     * @return the number of remaining complete months beyond the years
     */
    public int getMonths() {
        return months;
    }

    /**
     * Returns the days component of the calculated age.
     *
     * @return the number of remaining days beyond the months
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns a formatted string representation of the age result.
     *
     * <p>The output follows the exact format required by the application:
     * {@code "Your age is X years, Y months, and Z days."} where X, Y, and Z
     * are replaced with the actual years, months, and days values respectively.</p>
     *
     * @return the formatted age string, e.g. {@code "Your age is 27 years, 6 months, and 15 days."}
     */
    @Override
    public String toString() {
        return "Your age is " + years + " years, " + months + " months, and " + days + " days.";
    }
}
