package com.agecalculator;

import java.time.LocalDate;

import com.agecalculator.model.AgeResult;
import com.agecalculator.service.AgeCalculatorService;
import com.agecalculator.util.DateValidator;
import com.agecalculator.util.InputHandler;

/**
 * Main entry point for the Age Calculator console application.
 *
 * <p>This class orchestrates the entire user interaction flow for calculating
 * a person's exact age from their date of birth. The execution follows a
 * straightforward linear sequence:</p>
 * <ol>
 *   <li>Prompt the user for their date of birth in DD/MM/YYYY format</li>
 *   <li>Validate and parse the input into a {@link LocalDate}</li>
 *   <li>Calculate the exact age in years, months, and days</li>
 *   <li>Display the formatted age result and optional enhancement metrics</li>
 * </ol>
 *
 * <p>This class adheres to the Single Responsibility Principle — it only
 * coordinates the flow between the input handler, validator, and calculator
 * service. All business logic, validation rules, and I/O management are
 * delegated to their respective classes.</p>
 *
 * <p>Error handling follows a layered approach: {@link DateValidator} throws
 * {@link IllegalArgumentException} for invalid or future dates, which this
 * class catches and presents as user-friendly messages. A generic
 * {@link Exception} catch serves as a safety net for any unexpected errors.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see AgeResult
 * @see AgeCalculatorService
 * @see DateValidator
 * @see InputHandler
 */
public class AgeCalculatorApp {

    /**
     * Application entry point that drives the age calculation workflow.
     *
     * <p>Executes a single calculation cycle: prompts the user for their date
     * of birth, validates the input, computes the exact age, and displays the
     * result along with optional enhancement metrics (total months, total days,
     * and countdown to next birthday). The application then exits.</p>
     *
     * <p>All exceptions are caught and translated into human-readable error
     * messages printed to {@code System.err}. The {@link InputHandler} resource
     * is guaranteed to be closed via a {@code finally} block regardless of
     * whether the operation succeeds or fails.</p>
     *
     * @param args command-line arguments (not used by this application)
     */
    public static void main(String[] args) {
        // Create the input handler to manage console I/O with Scanner
        InputHandler inputHandler = new InputHandler();

        try {
            // Step 1: Prompt the user and read the date of birth input
            String dobString = inputHandler.readInput("Enter your Date of Birth (DD/MM/YYYY): ");

            // Step 2: Validate and parse the input into a LocalDate object.
            // DateValidator.parseAndValidate() will throw IllegalArgumentException
            // if the input is null/empty, has an invalid format, represents a
            // non-existent calendar date, or is a future date.
            LocalDate dob = DateValidator.parseAndValidate(dobString);

            // Step 3: Create the calculation service and compute the exact age
            AgeCalculatorService service = new AgeCalculatorService();
            AgeResult result = service.calculateAge(dob);

            // Step 4: Display the primary age result in the required format
            // AgeResult.toString() produces: "Your age is X years, Y months, and Z days."
            System.out.println(result.toString());

            // Step 5: Display optional enhancement metrics on separate lines
            long totalMonths = service.getTotalMonths(dob);
            System.out.println("Total age in months: " + totalMonths + " months");

            long totalDays = service.getTotalDays(dob);
            System.out.println("Total age in days: " + totalDays + " days");

            long daysUntilBirthday = service.getDaysUntilNextBirthday(dob);
            System.out.println("Days until next birthday: " + daysUntilBirthday + " days");

        } catch (IllegalArgumentException e) {
            // Handle validation errors from DateValidator with the user-friendly
            // message already embedded in the exception
            System.err.println(e.getMessage());
        } catch (Exception e) {
            // Safety net for any unexpected errors — display a generic message
            // without exposing raw stack traces to the user
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            // Guarantee resource cleanup: close the Scanner wrapped by InputHandler
            // to prevent resource leaks regardless of success or failure
            inputHandler.close();
        }
    }
}
