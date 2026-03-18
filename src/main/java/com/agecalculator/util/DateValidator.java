package com.agecalculator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import com.agecalculator.exception.FutureDateException;
import com.agecalculator.exception.InvalidDateFormatException;

/**
 * Utility class for validating and parsing Date of Birth input strings.
 *
 * <p>This class provides static methods to validate date input in {@code DD/MM/YYYY}
 * format. It rejects future dates, invalid calendar dates (e.g., {@code 31/02/2020}),
 * and malformed input strings, providing meaningful error messages for each failure case.</p>
 *
 * <p>This class follows the <strong>Utility Class Pattern</strong>: it contains only
 * static methods, declares a private constructor to prevent instantiation, and is
 * marked {@code final} to prevent subclassing.</p>
 *
 * <p>The date parsing uses {@link DateTimeFormatter} with {@link ResolverStyle#STRICT}
 * to ensure that only valid calendar dates are accepted. For example, {@code "29/02/2001"}
 * is rejected because 2001 is not a leap year, while {@code "29/02/2000"} is accepted
 * because 2000 is a leap year.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * LocalDate dob = DateValidator.parseAndValidate("15/08/1998");
 * // dob is LocalDate.of(1998, 8, 15)
 * }</pre>
 *
 * @see com.agecalculator.exception.FutureDateException
 * @see com.agecalculator.exception.InvalidDateFormatException
 */
public final class DateValidator {

    /**
     * Date formatter configured for the {@code DD/MM/YYYY} input pattern with strict
     * date resolution. Uses {@code "dd/MM/uuuu"} (proleptic year) instead of
     * {@code "dd/MM/yyyy"} because {@link ResolverStyle#STRICT} requires the use of
     * {@code uuuu} for proper year validation.
     *
     * <p>With {@link ResolverStyle#STRICT}, the formatter validates that the day-of-month
     * is valid for the given month and year. For example, {@code "31/02/2020"} is rejected
     * because February never has 31 days, and {@code "29/02/2001"} is rejected because
     * 2001 is not a leap year.</p>
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * <p>All functionality is accessed through static methods.</p>
     */
    private DateValidator() {
        // Utility class — instantiation not allowed
    }

    /**
     * Parses and validates a date string in {@code DD/MM/YYYY} format.
     *
     * <p>This method performs three levels of validation in the following order:</p>
     * <ol>
     *   <li><strong>Null/Empty check:</strong> Rejects {@code null} or blank input with
     *       an {@link InvalidDateFormatException}.</li>
     *   <li><strong>Format and calendar validation:</strong> Parses the input using
     *       {@link DateTimeFormatter} with {@link ResolverStyle#STRICT}. Invalid formats
     *       (e.g., {@code "2020-08-15"}) and impossible dates (e.g., {@code "31/02/2020"})
     *       cause a {@link DateTimeParseException}, which is caught and wrapped in an
     *       {@link InvalidDateFormatException} to preserve the cause chain.</li>
     *   <li><strong>Future date check:</strong> Verifies that the parsed date is not after
     *       the current system date ({@link LocalDate#now()}).</li>
     * </ol>
     *
     * @param input the date string to parse and validate, expected in {@code DD/MM/YYYY} format
     * @return a {@link LocalDate} representing the parsed and validated date, guaranteed to be
     *         a valid calendar date that is not in the future
     * @throws InvalidDateFormatException if the input is {@code null}, empty, blank, not in
     *         {@code DD/MM/YYYY} format, or represents an impossible calendar date
     * @throws FutureDateException if the parsed date is after the current system date
     */
    public static LocalDate parseAndValidate(String input) {
        // Step 1: Null/empty input check — must come before any parsing attempt
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidDateFormatException("Date input cannot be null or empty.");
        }

        // Step 2: Parse the date string with strict format and calendar validation
        LocalDate date;
        try {
            date = LocalDate.parse(input.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(
                    "Invalid date format. Please enter date in DD/MM/YYYY format.", e);
        }

        // Step 3: Semantic validation — reject future dates
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateException("Date of birth cannot be in the future: " + input.trim());
        }

        // All validations passed — return the valid date
        return date;
    }
}
