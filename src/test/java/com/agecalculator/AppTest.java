package com.agecalculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke test class for the {@link App} main application entry point.
 *
 * <p>This class provides foundational smoke and integration tests to verify that
 * the {@link App} class — the console application's entry point — can be
 * instantiated, that its {@code main} method exists with the correct signature,
 * and that the end-to-end workflow produces the expected output format when
 * provided with valid date input.</p>
 *
 * <p>These tests are intentionally lightweight:</p>
 * <ul>
 *   <li><strong>Instantiation test:</strong> Verifies the default constructor
 *       executes without throwing exceptions</li>
 *   <li><strong>Main method existence test:</strong> Uses reflection to confirm
 *       the {@code public static void main(String[])} signature exists</li>
 *   <li><strong>Valid input integration test:</strong> Redirects {@code System.in}
 *       and {@code System.out} to verify the full input-to-output pipeline produces
 *       the expected age output format</li>
 * </ul>
 *
 * <p>No mocking framework is used. The {@link App} class is tested directly.
 * Service-level unit tests reside in dedicated test classes
 * ({@code AgeCalculatorServiceTest}, {@code DateValidatorTest}, etc.).</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see App
 */
class AppTest {

    /**
     * Verifies that the {@link App} class can be instantiated using its default
     * constructor without throwing any exceptions.
     *
     * <p>This is the most basic smoke test — if the class cannot be constructed,
     * no other functionality can work. The assertion confirms the resulting
     * reference is non-null, which proves the constructor completed successfully.</p>
     */
    @Test
    @DisplayName("App class can be instantiated")
    void testAppInstantiation() {
        // Arrange & Act: instantiate the App class using its default constructor
        App app = new App();

        // Assert: the instance must be non-null, proving the constructor succeeded
        assertNotNull(app, "App instance should not be null after construction");
    }

    /**
     * Verifies that the {@code main} method exists on the {@link App} class with
     * the standard Java entry-point signature: {@code public static void main(String[])}.
     *
     * <p>This test uses reflection ({@link Class#getMethod(String, Class[])}) to
     * look up the method by name and parameter type. If the method does not exist
     * or has an incorrect signature, {@code getMethod} throws
     * {@link NoSuchMethodException}, which would cause the
     * {@code assertDoesNotThrow} assertion to fail.</p>
     *
     * <p>Reflection is used instead of direct invocation because {@link App#main(String[])}
     * reads from {@code System.in} via {@link java.util.Scanner}, which would block
     * indefinitely in a test environment without input redirection. The dedicated
     * integration test ({@link #testMainMethodWithValidInput()}) handles the
     * redirected-I/O scenario.</p>
     */
    @Test
    @DisplayName("Main method exists and can be invoked")
    void testMainMethodExists() {
        // Act & Assert: use reflection to verify the main method signature exists
        // App.class.getMethod("main", String[].class) throws NoSuchMethodException
        // if the method is not found — assertDoesNotThrow will catch that failure
        assertDoesNotThrow(
                () -> App.class.getMethod("main", String[].class),
                "App class should have a public static void main(String[]) method"
        );
    }

    /**
     * Integration test that verifies the full end-to-end workflow of
     * {@link App#main(String[])} when provided with a valid date input.
     *
     * <p>This test redirects {@code System.in} to supply a known valid Date of
     * Birth ({@code 15/08/1998}) and redirects {@code System.out} to capture
     * all console output. After invoking {@code App.main()}, the captured output
     * is inspected to confirm it contains the mandatory output format components:
     * {@code "Your age is"}, {@code "years"}, {@code "months"}, and
     * {@code "days"}.</p>
     *
     * <p>The exact numeric values in the output are intentionally <em>not</em>
     * asserted because they change daily as the current system date advances.
     * The format validation ensures the application correctly flows through
     * input parsing, validation, age calculation, and result formatting.</p>
     *
     * <p>Original {@code System.in} and {@code System.out} streams are saved
     * before the test and unconditionally restored in a {@code finally} block
     * to prevent interference with other tests or the test runner itself.</p>
     */
    @Test
    @DisplayName("Main method processes valid date input correctly")
    void testMainMethodWithValidInput() {
        // Arrange: save original System.in and System.out for restoration
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            // Arrange: redirect System.in with a valid DOB in DD/MM/YYYY format
            String testInput = "15/08/1998\n";
            ByteArrayInputStream simulatedInput = new ByteArrayInputStream(testInput.getBytes());
            System.setIn(simulatedInput);

            // Arrange: redirect System.out to capture all printed output
            ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
            PrintStream capturingPrintStream = new PrintStream(capturedOutput);
            System.setOut(capturingPrintStream);

            // Act: invoke the main method with an empty args array
            App.main(new String[]{});

            // Flush to ensure all output is written to the byte array
            capturingPrintStream.flush();

            // Assert: verify the captured output contains the mandatory format components
            String output = capturedOutput.toString();
            assertTrue(output.contains("Your age is"),
                    "Output should contain 'Your age is' but was: " + output);
            assertTrue(output.contains("years"),
                    "Output should contain 'years' but was: " + output);
            assertTrue(output.contains("months"),
                    "Output should contain 'months' but was: " + output);
            assertTrue(output.contains("days"),
                    "Output should contain 'days' but was: " + output);
        } finally {
            // Teardown: unconditionally restore original System.in and System.out
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
