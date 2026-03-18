package com.agecalculator.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import com.agecalculator.model.AgeResult;
import com.agecalculator.service.AgeCalculatorService;

/**
 * Test class for {@link AgeCalculatorService} that validates the core age
 * calculation business logic across multiple scenarios.
 *
 * <p>This class uses a main-method-based approach for test execution without
 * requiring any external test frameworks (no JUnit, no TestNG). Each test
 * method is invoked from {@link #main(String[])} and prints pass/fail
 * results directly to the console.</p>
 *
 * <p>Test coverage includes:</p>
 * <ul>
 *   <li>Normal DOB calculation (15/08/1998)</li>
 *   <li>Leap year DOB calculation (29/02/2000)</li>
 *   <li>Today's date as DOB (zero-age edge case)</li>
 *   <li>Total months computation</li>
 *   <li>Total days computation</li>
 *   <li>Next birthday countdown (range-based validation)</li>
 * </ul>
 *
 * <p>Expected values for date-dependent tests are computed dynamically using
 * {@link Period#between(LocalDate, LocalDate)} and {@link ChronoUnit} to
 * ensure correctness regardless of the date the tests are executed.</p>
 *
 * <p><strong>Compilation:</strong>
 * {@code javac -d out -cp out src/test/java/com/agecalculator/service/AgeCalculatorServiceTest.java}</p>
 * <p><strong>Execution:</strong>
 * {@code java -cp out com.agecalculator.service.AgeCalculatorServiceTest}</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class AgeCalculatorServiceTest {

    /** Counter tracking the number of test assertions that passed. */
    private static int testsPassed = 0;

    /** Counter tracking the number of test assertions that failed. */
    private static int testsFailed = 0;

    // -------------------------------------------------------------------------
    // Assertion Helper Methods
    // -------------------------------------------------------------------------

    /**
     * Asserts that two values are equal using {@link Object#equals(Object)}.
     *
     * <p>Prints a PASS message and increments {@code testsPassed} if the values
     * are equal, or prints a FAIL message with expected vs actual details and
     * increments {@code testsFailed} if they differ.</p>
     *
     * @param testName a descriptive name for the assertion being checked
     * @param expected the expected value (must not be null)
     * @param actual   the actual value produced by the code under test
     */
    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected.equals(actual)) {
            System.out.println("  PASS: " + testName);
            testsPassed++;
        } else {
            System.out.println("  FAIL: " + testName + " — Expected: " + expected + ", Got: " + actual);
            testsFailed++;
        }
    }

    /**
     * Asserts that a boolean condition is true.
     *
     * <p>Prints a PASS message and increments {@code testsPassed} if the
     * condition is true, or prints a FAIL message and increments
     * {@code testsFailed} if it is false.</p>
     *
     * @param testName  a descriptive name for the assertion being checked
     * @param condition the boolean condition to evaluate
     */
    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            testsPassed++;
        } else {
            System.out.println("  FAIL: " + testName);
            testsFailed++;
        }
    }

    // -------------------------------------------------------------------------
    // Test Methods
    // -------------------------------------------------------------------------

    /**
     * Tests age calculation for a normal date of birth (15/08/1998).
     *
     * <p>Verifies that the years, months, and days components returned by
     * {@link AgeCalculatorService#calculateAge(LocalDate)} match the values
     * independently computed using {@link Period#between(LocalDate, LocalDate)}.
     * Expected values are calculated dynamically to ensure correctness
     * regardless of the current date.</p>
     */
    private static void testNormalDob() {
        System.out.println("Running testNormalDob...");

        // Create a fresh service instance for this test
        AgeCalculatorService service = new AgeCalculatorService();

        // Define the DOB from the user example: 15/08/1998
        LocalDate dob = LocalDate.of(1998, 8, 15);

        // Calculate age using the service under test
        AgeResult result = service.calculateAge(dob);

        // Independently compute expected values using Period.between()
        Period expected = Period.between(dob, LocalDate.now());

        // Verify each component matches the independently computed expectation
        assertEquals("years component", expected.getYears(), result.getYears());
        assertEquals("months component", expected.getMonths(), result.getMonths());
        assertEquals("days component", expected.getDays(), result.getDays());
    }

    /**
     * Tests age calculation for a leap year date of birth (29/02/2000).
     *
     * <p>Validates that {@link AgeCalculatorService#calculateAge(LocalDate)}
     * correctly handles a DOB on February 29 in a leap year. The expected
     * values are computed dynamically using {@link Period#between(LocalDate, LocalDate)}
     * to account for the current date potentially being in a non-leap year.</p>
     */
    private static void testLeapYearDob() {
        System.out.println("Running testLeapYearDob...");

        // Create a fresh service instance for this test
        AgeCalculatorService service = new AgeCalculatorService();

        // Define a leap year DOB: February 29, 2000
        LocalDate dob = LocalDate.of(2000, 2, 29);

        // Calculate age using the service under test
        AgeResult result = service.calculateAge(dob);

        // Independently compute expected values using Period.between()
        Period expected = Period.between(dob, LocalDate.now());

        // Verify each component matches — Period.between() handles leap years correctly
        assertEquals("leap year - years", expected.getYears(), result.getYears());
        assertEquals("leap year - months", expected.getMonths(), result.getMonths());
        assertEquals("leap year - days", expected.getDays(), result.getDays());
    }

    /**
     * Tests age calculation when the DOB is today's date (zero-age edge case).
     *
     * <p>Validates that when the date of birth is exactly the current date,
     * all age components (years, months, days) are zero. This is a critical
     * boundary condition for the age calculation logic.</p>
     */
    private static void testTodayDob() {
        System.out.println("Running testTodayDob...");

        // Create a fresh service instance for this test
        AgeCalculatorService service = new AgeCalculatorService();

        // Use today's date as the DOB — should yield zero age
        LocalDate dob = LocalDate.now();

        // Calculate age using the service under test
        AgeResult result = service.calculateAge(dob);

        // All components must be exactly zero for a DOB of today
        assertEquals("today DOB - years", 0, result.getYears());
        assertEquals("today DOB - months", 0, result.getMonths());
        assertEquals("today DOB - days", 0, result.getDays());
    }

    /**
     * Tests the total months calculation for a normal DOB (15/08/1998).
     *
     * <p>Verifies that {@link AgeCalculatorService#getTotalMonths(LocalDate)}
     * returns the same total month count as independently computed using
     * {@link ChronoUnit#MONTHS}. This validates the optional enhancement
     * for displaying total age in months.</p>
     */
    private static void testTotalMonths() {
        System.out.println("Running testTotalMonths...");

        // Create a fresh service instance for this test
        AgeCalculatorService service = new AgeCalculatorService();

        // Use the same DOB as the user example
        LocalDate dob = LocalDate.of(1998, 8, 15);

        // Calculate total months using the service under test
        long result = service.getTotalMonths(dob);

        // Independently compute expected total months using ChronoUnit
        long expected = ChronoUnit.MONTHS.between(dob, LocalDate.now());

        // Verify the service result matches the independent calculation
        assertEquals("total months", expected, result);
    }

    /**
     * Tests the total days calculation for a normal DOB (15/08/1998).
     *
     * <p>Verifies that {@link AgeCalculatorService#getTotalDays(LocalDate)}
     * returns the same total day count as independently computed using
     * {@link ChronoUnit#DAYS}. This validates the optional enhancement
     * for displaying total age in days.</p>
     */
    private static void testTotalDays() {
        System.out.println("Running testTotalDays...");

        // Create a fresh service instance for this test
        AgeCalculatorService service = new AgeCalculatorService();

        // Use the same DOB as the user example
        LocalDate dob = LocalDate.of(1998, 8, 15);

        // Calculate total days using the service under test
        long result = service.getTotalDays(dob);

        // Independently compute expected total days using ChronoUnit
        long expected = ChronoUnit.DAYS.between(dob, LocalDate.now());

        // Verify the service result matches the independent calculation
        assertEquals("total days", expected, result);
    }

    /**
     * Tests the days-until-next-birthday calculation for a normal DOB (15/08/1998).
     *
     * <p>Verifies that {@link AgeCalculatorService#getDaysUntilNextBirthday(LocalDate)}
     * returns a value within the valid range of 0–366 days. The exact value
     * varies depending on the current date, so only range-based assertions
     * are used. The upper bound of 366 accounts for leap year scenarios.</p>
     */
    private static void testDaysUntilNextBirthday() {
        System.out.println("Running testDaysUntilNextBirthday...");

        // Create a fresh service instance for this test
        AgeCalculatorService service = new AgeCalculatorService();

        // Use the same DOB as the user example
        LocalDate dob = LocalDate.of(1998, 8, 15);

        // Calculate days until next birthday using the service under test
        long result = service.getDaysUntilNextBirthday(dob);

        // The countdown must be non-negative (0 or more days)
        assertTrue("next birthday >= 0", result >= 0);

        // The countdown must not exceed 366 days (accounts for leap years)
        assertTrue("next birthday <= 366", result <= 366);
    }

    // -------------------------------------------------------------------------
    // Main Entry Point — Test Runner
    // -------------------------------------------------------------------------

    /**
     * Entry point that executes all test methods and prints a summary.
     *
     * <p>All test methods are called in a predetermined order. After all
     * tests complete, a summary line shows the total passed and failed
     * assertions. If any assertion failed, the process exits with code 1
     * to signal failure to calling scripts or CI systems.</p>
     *
     * <p>The entire test execution is wrapped in a try-catch block to
     * gracefully handle unexpected exceptions, printing the error message
     * and exiting with code 1.</p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            System.out.println("============================================");
            System.out.println("AgeCalculatorService Test Suite");
            System.out.println("============================================");

            // Execute all test methods in the specified order
            testNormalDob();
            testLeapYearDob();
            testTodayDob();
            testTotalMonths();
            testTotalDays();
            testDaysUntilNextBirthday();

            // Print summary of test results
            System.out.println("============================================");
            System.out.println("Results: " + testsPassed + " passed, " + testsFailed + " failed");
            System.out.println("============================================");

            // Exit with error code if any tests failed
            if (testsFailed > 0) {
                System.exit(1);
            }
        } catch (Exception e) {
            // Catch any unexpected exception to prevent raw stack traces
            System.err.println("Unexpected error during test execution: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
