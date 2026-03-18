package com.agecalculator.util;

import com.agecalculator.util.DateValidator;
import java.time.LocalDate;

/**
 * Test class for {@link DateValidator} that validates all input validation scenarios.
 *
 * <p>This class uses a main-method-based approach with pass/fail output printed to
 * {@code System.out} instead of relying on external test frameworks such as JUnit
 * or TestNG. Each test method exercises a specific validation scenario of
 * {@link DateValidator#parseAndValidate(String)} and reports its result.</p>
 *
 * <p>Test scenarios covered:</p>
 * <ul>
 *     <li>Valid past date parsing and correct {@link LocalDate} return value</li>
 *     <li>Invalid calendar date rejection (e.g., 31/02/2020)</li>
 *     <li>Future date rejection with dynamic date generation</li>
 *     <li>Wrong date format rejection (e.g., YYYY-MM-DD instead of DD/MM/YYYY)</li>
 *     <li>Empty and null input rejection</li>
 *     <li>Leap year valid date acceptance (Feb 29 in a leap year)</li>
 *     <li>Leap year invalid date rejection (Feb 29 in a non-leap year)</li>
 * </ul>
 *
 * @see DateValidator
 * @see DateValidator#parseAndValidate(String)
 */
public class DateValidatorTest {

    /** Counter for tests that passed successfully. */
    private static int passed = 0;

    /** Counter for tests that failed. */
    private static int failed = 0;

    /**
     * Entry point that runs all test methods sequentially and prints a summary
     * of the overall test results (total passed and failed counts).
     *
     * <p>If any test fails, the process exits with a non-zero exit code (1)
     * to signal failure to calling scripts or CI environments.</p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== DateValidator Tests ===");
        System.out.println();

        testValidDate();
        testInvalidDate();
        testFutureDate();
        testWrongFormat();
        testEmptyInput();
        testLeapYearValid();
        testLeapYearInvalid();

        System.out.println();
        System.out.println("Results: " + passed + " passed, " + failed + " failed");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // -----------------------------------------------------------------------
    // Helper methods for consistent test result reporting
    // -----------------------------------------------------------------------

    /**
     * Records a passing test and prints the result to standard output.
     *
     * @param testName the name of the test that passed
     */
    private static void assertPass(String testName) {
        System.out.println("PASS: " + testName);
        passed++;
    }

    /**
     * Records a failing test and prints the result with a reason to standard output.
     *
     * @param testName the name of the test that failed
     * @param reason   a brief description of why the test failed
     */
    private static void assertFail(String testName, String reason) {
        System.out.println("FAIL: " + testName + " - " + reason);
        failed++;
    }

    // -----------------------------------------------------------------------
    // Test methods — each exercises a specific DateValidator scenario
    // -----------------------------------------------------------------------

    /**
     * Tests that a valid past date string is correctly parsed into the expected
     * {@link LocalDate} without throwing any exception.
     *
     * <p>Input: {@code "15/08/1998"} → Expected: {@code LocalDate.of(1998, 8, 15)}</p>
     */
    private static void testValidDate() {
        String testName = "testValidDate";
        try {
            // Parse a known valid past date
            LocalDate result = DateValidator.parseAndValidate("15/08/1998");
            LocalDate expected = LocalDate.of(1998, 8, 15);

            if (result.equals(expected)) {
                assertPass(testName);
            } else {
                assertFail(testName, "Expected " + expected + " but got " + result);
            }
        } catch (Exception e) {
            assertFail(testName, "Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests that a non-existent calendar date (February 31) is rejected with an
     * {@link IllegalArgumentException}.
     *
     * <p>Input: {@code "31/02/2020"} → Expected: {@code IllegalArgumentException}</p>
     */
    private static void testInvalidDate() {
        String testName = "testInvalidDate";
        try {
            // 31/02/2020 does not exist on any calendar — should be rejected
            DateValidator.parseAndValidate("31/02/2020");
            assertFail(testName, "Expected IllegalArgumentException but no exception was thrown");
        } catch (IllegalArgumentException e) {
            // Correct behaviour — the invalid date was properly rejected
            assertPass(testName);
        } catch (Exception e) {
            assertFail(testName, "Expected IllegalArgumentException but got " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Tests that a date in the future is rejected with an {@link IllegalArgumentException}
     * whose message contains the word "future".
     *
     * <p>The future date is dynamically generated using {@code LocalDate.now().plusYears(1)}
     * to ensure the test never becomes stale by relying on a hardcoded future date.</p>
     */
    private static void testFutureDate() {
        String testName = "testFutureDate";
        try {
            // Dynamically generate a date that is always one year in the future
            LocalDate futureDate = LocalDate.now().plusYears(1);
            String futureDateStr = String.format("%02d/%02d/%04d",
                    futureDate.getDayOfMonth(),
                    futureDate.getMonthValue(),
                    futureDate.getYear());

            DateValidator.parseAndValidate(futureDateStr);
            assertFail(testName, "Expected IllegalArgumentException for future date '" + futureDateStr + "' but no exception was thrown");
        } catch (IllegalArgumentException e) {
            // Verify the error message mentions "future" (case-insensitive)
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("future")) {
                assertPass(testName);
            } else {
                assertFail(testName, "IllegalArgumentException thrown but message does not mention 'future': " + e.getMessage());
            }
        } catch (Exception e) {
            assertFail(testName, "Expected IllegalArgumentException but got " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Tests that a date string in the wrong format (YYYY-MM-DD instead of DD/MM/YYYY)
     * is rejected with an {@link IllegalArgumentException}.
     *
     * <p>Input: {@code "1998-08-15"} → Expected: {@code IllegalArgumentException}</p>
     */
    private static void testWrongFormat() {
        String testName = "testWrongFormat";
        try {
            // YYYY-MM-DD format should be rejected — only DD/MM/YYYY is accepted
            DateValidator.parseAndValidate("1998-08-15");
            assertFail(testName, "Expected IllegalArgumentException but no exception was thrown");
        } catch (IllegalArgumentException e) {
            // Correct behaviour — the wrong format was properly rejected
            assertPass(testName);
        } catch (Exception e) {
            assertFail(testName, "Expected IllegalArgumentException but got " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Tests that both empty string and null input are rejected with an
     * {@link IllegalArgumentException}.
     *
     * <p>Both empty ({@code ""}) and null inputs must be validated and rejected
     * to prevent downstream parsing errors.</p>
     */
    private static void testEmptyInput() {
        String testName = "testEmptyInput";
        boolean emptyPassed = false;
        boolean nullPassed = false;

        // Sub-test 1: Empty string input
        try {
            DateValidator.parseAndValidate("");
            assertFail(testName, "Expected IllegalArgumentException for empty string but no exception was thrown");
            return;
        } catch (IllegalArgumentException e) {
            // Empty string correctly rejected
            emptyPassed = true;
        } catch (Exception e) {
            assertFail(testName, "Expected IllegalArgumentException for empty string but got " + e.getClass().getSimpleName());
            return;
        }

        // Sub-test 2: Null input
        try {
            DateValidator.parseAndValidate(null);
            assertFail(testName, "Expected IllegalArgumentException for null input but no exception was thrown");
            return;
        } catch (IllegalArgumentException e) {
            // Null input correctly rejected
            nullPassed = true;
        } catch (Exception e) {
            assertFail(testName, "Expected IllegalArgumentException for null input but got " + e.getClass().getSimpleName());
            return;
        }

        // Both sub-tests must pass for the overall test to pass
        if (emptyPassed && nullPassed) {
            assertPass(testName);
        } else {
            assertFail(testName, "Empty passed=" + emptyPassed + ", null passed=" + nullPassed);
        }
    }

    /**
     * Tests that February 29 in a leap year (2000) is accepted as a valid date
     * and parsed into the correct {@link LocalDate}.
     *
     * <p>Input: {@code "29/02/2000"} → Expected: {@code LocalDate.of(2000, 2, 29)}</p>
     */
    private static void testLeapYearValid() {
        String testName = "testLeapYearValid";
        try {
            // 2000 is a leap year — Feb 29 should be accepted
            LocalDate result = DateValidator.parseAndValidate("29/02/2000");
            LocalDate expected = LocalDate.of(2000, 2, 29);

            if (result.equals(expected)) {
                assertPass(testName);
            } else {
                assertFail(testName, "Expected " + expected + " but got " + result);
            }
        } catch (Exception e) {
            assertFail(testName, "Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests that February 29 in a non-leap year (2023) is rejected with an
     * {@link IllegalArgumentException}.
     *
     * <p>Input: {@code "29/02/2023"} → Expected: {@code IllegalArgumentException}</p>
     */
    private static void testLeapYearInvalid() {
        String testName = "testLeapYearInvalid";
        try {
            // 2023 is NOT a leap year — Feb 29 should be rejected
            DateValidator.parseAndValidate("29/02/2023");
            assertFail(testName, "Expected IllegalArgumentException but no exception was thrown");
        } catch (IllegalArgumentException e) {
            // Correct behaviour — non-existent leap year date was properly rejected
            assertPass(testName);
        } catch (Exception e) {
            assertFail(testName, "Expected IllegalArgumentException but got " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
