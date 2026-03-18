package com.agecalculator;

import java.time.LocalDate;
import java.util.Scanner;

import com.agecalculator.exception.FutureDateException;
import com.agecalculator.exception.InvalidDateFormatException;
import com.agecalculator.model.AgeResult;
import com.agecalculator.service.AgeCalculatorService;
import com.agecalculator.service.BirthdayCountdownService;
import com.agecalculator.util.DateValidator;

/**
 * Main entry point for the Age Calculator console application.
 *
 * <p>This class orchestrates the user interaction flow for calculating a person's
 * exact age based on their Date of Birth. It reads user input from the console,
 * delegates validation to {@link DateValidator}, performs the age calculation via
 * {@link AgeCalculatorService}, and displays the formatted result to the user.</p>
 *
 * <p>As an enhancement, the application also calculates and displays the number
 * of days remaining until the user's next birthday using
 * {@link BirthdayCountdownService}.</p>
 *
 * <p>This class contains no business logic — it is purely an I/O and orchestration
 * layer. All date parsing, validation, and calculation responsibilities are
 * delegated to the appropriate service and utility classes.</p>
 *
 * <p><strong>Input format:</strong> {@code DD/MM/YYYY} (e.g., {@code 15/08/1998})</p>
 * <p><strong>Output format:</strong> {@code Your age is X years, Y months, and Z days.}</p>
 *
 * <p><strong>Error handling:</strong> The application catches
 * {@link InvalidDateFormatException} for malformed or impossible dates,
 * {@link FutureDateException} for dates in the future, and a generic
 * {@link Exception} catch-all to ensure no unhandled stack trace is ever
 * displayed to the user.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see DateValidator
 * @see AgeCalculatorService
 * @see BirthdayCountdownService
 * @see AgeResult
 */
public class App {

    /**
     * Application entry point that drives the age calculation workflow.
     *
     * <p>Execution flow:</p>
     * <ol>
     *   <li>Prompts the user to enter their Date of Birth in {@code DD/MM/YYYY} format</li>
     *   <li>Reads the input string from standard input via {@link Scanner}</li>
     *   <li>Validates and parses the input using {@link DateValidator#parseAndValidate(String)}</li>
     *   <li>Obtains the current system date via {@link LocalDate#now()}</li>
     *   <li>Calculates the exact age using {@link AgeCalculatorService#calculateAge(LocalDate, LocalDate)}</li>
     *   <li>Prints the age in the mandatory format via {@link AgeResult#toString()}</li>
     *   <li>Calculates and prints the days until the next birthday via
     *       {@link BirthdayCountdownService#daysUntilNextBirthday(LocalDate, LocalDate)}</li>
     * </ol>
     *
     * <p>The {@link Scanner} is managed using a try-with-resources statement to
     * guarantee proper resource cleanup. All exception handling is performed within
     * this method to ensure the application never crashes with an unhandled stack
     * trace visible to the user.</p>
     *
     * @param args command-line arguments (not used by this application)
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Step 1: Prompt the user for their Date of Birth
            System.out.print("Enter your Date of Birth (DD/MM/YYYY): ");

            // Step 2: Read the user input
            String input = scanner.nextLine();

            // Step 3: Validate and parse the input into a LocalDate
            LocalDate dob = DateValidator.parseAndValidate(input);

            // Step 4: Get the current system date
            LocalDate today = LocalDate.now();

            // Step 5: Calculate the exact age using the service
            AgeCalculatorService ageCalculatorService = new AgeCalculatorService();
            AgeResult ageResult = ageCalculatorService.calculateAge(dob, today);

            // Step 6: Display the formatted age result
            System.out.println(ageResult.toString());

            // Step 7: Calculate and display the birthday countdown (enhancement)
            BirthdayCountdownService birthdayCountdownService = new BirthdayCountdownService();
            long daysUntilNextBirthday = birthdayCountdownService.daysUntilNextBirthday(dob, today);
            System.out.println("Days until your next birthday: " + daysUntilNextBirthday);

        } catch (InvalidDateFormatException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (FutureDateException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
