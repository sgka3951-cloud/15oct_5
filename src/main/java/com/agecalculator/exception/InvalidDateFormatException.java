package com.agecalculator.exception;

/**
 * Custom unchecked exception thrown when a date input cannot be parsed as a valid date.
 *
 * <p>This exception is raised in the following scenarios:</p>
 * <ul>
 *   <li>The input string does not conform to the expected {@code DD/MM/YYYY} format
 *       (e.g., {@code "2020-08-15"}, {@code "abc"}, {@code ""})</li>
 *   <li>The input represents an impossible calendar date
 *       (e.g., {@code "31/02/2020"} — February never has 31 days)</li>
 *   <li>The input is {@code null}</li>
 * </ul>
 *
 * <p>This exception extends {@link RuntimeException} (unchecked), so callers are
 * not required to declare it in a {@code throws} clause. It supports wrapping a
 * root cause {@link Throwable} (typically a
 * {@link java.time.format.DateTimeParseException}) to preserve the original
 * stack trace and cause chain when the date parsing fails.</p>
 *
 * <p>This class is used by {@code DateValidator} in the
 * {@code com.agecalculator.util} package, which catches
 * {@link java.time.format.DateTimeParseException} from the
 * {@link java.time.format.DateTimeFormatter} and wraps it in this
 * domain-specific exception.</p>
 *
 * @see java.time.format.DateTimeParseException
 * @see java.time.format.DateTimeFormatter
 */
public class InvalidDateFormatException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidDateFormatException} with the specified
     * detail message.
     *
     * @param message the detail message describing why the date is invalid
     *                (e.g., {@code "Invalid date format. Please use DD/MM/YYYY."})
     */
    public InvalidDateFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidDateFormatException} with the specified
     * detail message and root cause.
     *
     * <p>This constructor implements the wrapping pattern, allowing
     * {@code DateValidator} to catch a {@link java.time.format.DateTimeParseException}
     * and wrap it in this domain-specific exception while preserving the
     * original stack trace and cause chain.</p>
     *
     * @param message the detail message describing the validation failure
     * @param cause   the underlying cause of this exception (typically a
     *                {@link java.time.format.DateTimeParseException} from the
     *                {@link java.time.format.DateTimeFormatter})
     */
    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
