package com.agecalculator.model;

/**
 * Immutable value object representing the result of an age calculation.
 *
 * <p>This class encapsulates a computed age as three integer components:
 * years, months, and days. It follows the Immutable Value Object pattern —
 * all fields are declared {@code final} and initialized exclusively through
 * the constructor. No setter (mutation) methods are provided, guaranteeing
 * that an {@code AgeResult} instance cannot be altered after creation.</p>
 *
 * <p>The {@link #toString()} method returns the age in the mandatory
 * human-readable format:
 * {@code "Your age is X years, Y months, and Z days."}</p>
 *
 * <p>This class has zero dependencies on other project classes and is used by:
 * <ul>
 *   <li>{@code AgeCalculatorService} — creates and returns {@code AgeResult} instances</li>
 *   <li>{@code App} — receives an {@code AgeResult} and calls {@code toString()} for display</li>
 * </ul>
 * </p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class AgeResult {

    /** The number of complete years in the calculated age. */
    private final int years;

    /** The number of remaining complete months in the calculated age (0–11). */
    private final int months;

    /** The number of remaining days in the calculated age (0–30). */
    private final int days;

    /**
     * Constructs a new {@code AgeResult} with the specified age components.
     *
     * <p>Once constructed, the values cannot be changed — this object is immutable.</p>
     *
     * @param years  the number of complete years in the age
     * @param months the number of remaining complete months (typically 0–11)
     * @param days   the number of remaining days (typically 0–30)
     */
    public AgeResult(int years, int months, int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }

    /**
     * Returns the number of complete years in the age.
     *
     * @return the number of complete years in the age
     */
    public int getYears() {
        return years;
    }

    /**
     * Returns the number of remaining complete months in the age.
     *
     * @return the number of remaining complete months (0–11)
     */
    public int getMonths() {
        return months;
    }

    /**
     * Returns the number of remaining days in the age.
     *
     * @return the number of remaining days (0–30)
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns a formatted string representing the age in the mandatory output format.
     *
     * <p>The returned string follows the exact format:
     * {@code "Your age is X years, Y months, and Z days."}
     * where X, Y, and Z are replaced with the respective age component values.</p>
     *
     * @return a formatted string in the format
     *         "Your age is X years, Y months, and Z days."
     */
    @Override
    public String toString() {
        return String.format("Your age is %d years, %d months, and %d days.", years, months, days);
    }
}
