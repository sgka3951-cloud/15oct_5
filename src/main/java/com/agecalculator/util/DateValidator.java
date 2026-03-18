package com.agecalculator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Static utility class for validating and parsing date of birth input strings.
 *
 * <p>This class provides strict date parsing and validation capabilities for the
 * Age Calculator application. It validates user-provided date strings against
 * the following criteria:</p>
 * <ul>
 *     <li>Invalid date formats (only DD/MM/YYYY is accepted)</li>
 *     <li>Non-existent calendar dates (e.g., 31/02/2020, 29/02/2023 for non-leap years)</li>
 *     <li>Future dates (date of birth cannot be after the current system date)</li>
 *     <li>Null or empty input strings</li>
 * </ul>
 *
 * <p>Internally, this class uses {@link DateTimeFormatter} configured with
 * {@link ResolverStyle#STRICT} to ensure precise date parsing. The strict resolver
 * rejects any date that does not correspond to a real calendar date, including
 * proper leap year enforcement.</p>
 *
 * <p>All methods in this class are static. No instance creation is needed or permitted.</p>
 *
 * @see java.time.LocalDate
 * @see java.time.format.DateTimeFormatter
 * @see java.time.format.ResolverStyle
 */
public class DateValidator {

    /**
     * Strict date formatter configured for the DD/MM/YYYY input pattern.
     *
     * <p>Uses the {@code uuuu} year pattern (proleptic year) instead of {@code yyyy}
     * (year-of-era) because {@link ResolverStyle#STRICT} requires {@code uuuu} for
     * correct strict validation. Using {@code yyyy} with STRICT resolution would
     * cause valid dates to fail parsing.</p>
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Private constructor to prevent instantiation of this utility class.
     * All access should be through the static methods provided.
     */
    private DateValidator() {
        // Utility class - prevent instantiation
    }

    /**
     * Parses and validates a date string in DD/MM/YYYY format.
     *
     * <p>This method performs validation in the following order:</p>
     * <ol>
     *     <li>Checks that the input is not null or empty</li>
     *     <li>Parses the input using a strict {@link DateTimeFormatter} that rejects
     *         invalid calendar dates (e.g., Feb 31, Feb 29 in non-leap years)</li>
     *     <li>Validates that the parsed date is not in the future</li>
     * </ol>
     *
     * @param dateString the date string to parse and validate (expected format: DD/MM/YYYY)
     * @return a validated {@link LocalDate} instance representing the parsed date
     * @throws IllegalArgumentException if the input is null, empty, in an invalid format,
     *         represents an invalid calendar date, or is in the future
     */
    public static LocalDate parseAndValidate(String dateString) {
        // Step 1: Reject null or empty input before attempting any parsing
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Date of birth cannot be empty. Please enter a date in DD/MM/YYYY format.");
        }

        // Step 2: Parse the trimmed input with the strict formatter.
        // ResolverStyle.STRICT ensures non-existent calendar dates
        // (e.g., 31/02/2020, 29/02/2023) are rejected with DateTimeParseException.
        LocalDate date;
        try {
            date = LocalDate.parse(dateString.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date format. Please use DD/MM/YYYY format.");
        }

        // Step 3: Ensure the parsed date is not in the future
        if (isFutureDate(date)) {
            throw new IllegalArgumentException(
                    "Date of birth cannot be in the future.");
        }

        // Step 4: Return the fully validated date
        return date;
    }

    /**
     * Checks whether the given date is after the current system date.
     *
     * @param date the date to check against the current system date
     * @return {@code true} if the date is strictly after today, {@code false} otherwise
     */
    private static boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
}
