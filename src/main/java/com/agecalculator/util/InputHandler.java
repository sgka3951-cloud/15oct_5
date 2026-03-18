package com.agecalculator.util;

import java.util.Scanner;

/**
 * Utility class that wraps {@link java.util.Scanner} for reading console input.
 *
 * <p>This class manages the lifecycle of a {@code Scanner} instance wrapping
 * {@code System.in}, providing a reusable method for prompting users and
 * reading trimmed input from the console. Resource cleanup is handled via
 * the {@link #close()} method, which should be called when the handler is
 * no longer needed to prevent resource leaks.</p>
 *
 * <p>This class follows the Single Responsibility Principle — it handles
 * only console I/O operations. Date parsing and input validation are
 * delegated to other classes in the application.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class InputHandler {

    /** The underlying Scanner instance used to read from standard input. */
    private final Scanner scanner;

    /**
     * Creates a new {@code InputHandler} instance that reads from standard
     * input ({@code System.in}).
     *
     * <p>The internal {@link Scanner} is initialized once and reused for all
     * subsequent calls to {@link #readInput(String)}.</p>
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the given prompt message and reads a line of input from the console.
     *
     * <p>The prompt is printed on the same line where user input will appear
     * (using {@code System.out.print} rather than {@code println}), matching
     * the expected interaction pattern:</p>
     * <pre>Enter your Date of Birth (DD/MM/YYYY): 15/08/1998</pre>
     *
     * <p>The returned input string is trimmed of leading and trailing whitespace
     * to normalize user input before further processing.</p>
     *
     * @param prompt the message to display to the user before reading input
     * @return the trimmed user input as a {@code String}
     */
    public String readInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input.trim();
    }

    /**
     * Closes the underlying {@link Scanner} to release system resources.
     *
     * <p>This method should be called when the {@code InputHandler} is no longer
     * needed, typically in a {@code finally} block or at the end of the
     * application lifecycle, to ensure that the {@code System.in} stream
     * resources are properly released.</p>
     */
    public void close() {
        scanner.close();
    }
}
