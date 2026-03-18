package com.agecalculator.exception;

/**
 * Custom unchecked exception thrown when a Date of Birth provided is in the future.
 *
 * <p>This exception is used by the date validation layer to signal that the user
 * has entered a date that occurs after the current system date, which is not a
 * valid Date of Birth. Since it extends {@link RuntimeException}, callers are
 * not forced to declare {@code throws FutureDateException} in their method
 * signatures.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * if (dateOfBirth.isAfter(LocalDate.now())) {
 *     throw new FutureDateException(
 *         "Date of birth cannot be in the future: " + dateOfBirth);
 * }
 * }</pre>
 *
 * @see java.lang.RuntimeException
 */
public class FutureDateException extends RuntimeException {

    /**
     * Constructs a new {@code FutureDateException} with the specified detail message.
     *
     * @param message the detail message describing why the date is invalid,
     *                for example {@code "Date of birth cannot be in the future: 01/01/2099"}
     */
    public FutureDateException(String message) {
        super(message);
    }
}
