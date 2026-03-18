# Java Age Calculator

A Java console application that calculates a user's exact age based on their Date of Birth (DOB). The application computes the elapsed time in years, months, and days, and displays the result in a human-readable format.

This project demonstrates core Java concepts including Object-Oriented Programming (OOP) principles, the `java.time` API for modern date handling, input validation patterns, and robust exception handling — all without any external dependencies.

## Features

- **DOB Input Parsing** — Accepts Date of Birth input in `DD/MM/YYYY` format with strict validation
- **Exact Age Calculation** — Calculates precise age in years, months, and days using `java.time.Period`
- **Input Validation** — Validates against invalid calendar dates (e.g., `31/02/2020`) and future dates
- **Total Age in Months and Days** — Displays the total age expressed as a single count of months and days
- **Next Birthday Countdown** — Shows the number of days remaining until the user's next birthday
- **OOP Design** — Follows Object-Oriented Programming principles with proper class decomposition and encapsulation
- **Leap Year Handling** — Correctly processes leap year dates (e.g., February 29)
- **User-Friendly Error Messages** — Provides clear, descriptive error messages via `try-catch` handling instead of raw stack traces

## Prerequisites

- **Java 17+** (OpenJDK recommended)
- No build tools (Maven/Gradle) required
- No external dependencies — uses only the Java Standard Library

Verify your Java installation:

```bash
java -version
```

Expected output should show version 17 or higher, for example:

```plaintext
openjdk version "17.0.18" 2026-01-20
```

## Project Structure

```
project-root/
├── README.md
├── .gitignore
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── agecalculator/
│   │               ├── AgeCalculatorApp.java        (Main entry point)
│   │               ├── model/
│   │               │   └── AgeResult.java           (Age result model)
│   │               ├── service/
│   │               │   └── AgeCalculatorService.java (Calculation logic)
│   │               └── util/
│   │                   ├── DateValidator.java        (Input validation)
│   │                   └── InputHandler.java         (Console I/O)
│   └── test/
│       └── java/
│           └── com/
│               └── agecalculator/
│                   ├── service/
│                   │   └── AgeCalculatorServiceTest.java
│                   └── util/
│                       └── DateValidatorTest.java
```

### Class Responsibilities

| Class | Package | Responsibility |
|-------|---------|----------------|
| **AgeCalculatorApp** | `com.agecalculator` | Main entry point; orchestrates the prompt → validate → calculate → display flow |
| **AgeResult** | `com.agecalculator.model` | Immutable data model holding age components (years, months, days) with formatted `toString()` output |
| **AgeCalculatorService** | `com.agecalculator.service` | Core business logic for age calculation, total months/days, and next birthday countdown |
| **DateValidator** | `com.agecalculator.util` | Strict date parsing and validation (format, calendar validity, future date check) |
| **InputHandler** | `com.agecalculator.util` | Console input wrapper around `java.util.Scanner` with resource lifecycle management |

## Build and Run

### Compile

Compile all source files into the `out` directory:

```bash
javac -d out src/main/java/com/agecalculator/model/AgeResult.java \
             src/main/java/com/agecalculator/util/DateValidator.java \
             src/main/java/com/agecalculator/util/InputHandler.java \
             src/main/java/com/agecalculator/service/AgeCalculatorService.java \
             src/main/java/com/agecalculator/AgeCalculatorApp.java
```

### Run

Execute the application:

```bash
java -cp out com.agecalculator.AgeCalculatorApp
```

## Usage Example

```plaintext
Enter your Date of Birth (DD/MM/YYYY): 15/08/1998

Your age is 27 years, 6 months, and 15 days.
Total age in months: 330 months
Total age in days: 10077 days
Days until next birthday: 150 days
```

> **Note:** The output values will vary based on the current system date.

## Error Handling

The application provides user-friendly error messages for all invalid input scenarios:

### Invalid Calendar Date

```plaintext
Enter your Date of Birth (DD/MM/YYYY): 31/02/2020
Invalid date format. Please use DD/MM/YYYY format.
```

### Future Date

```plaintext
Enter your Date of Birth (DD/MM/YYYY): 15/08/2030
Date of birth cannot be in the future.
```

### Wrong Format

```plaintext
Enter your Date of Birth (DD/MM/YYYY): 1998-08-15
Invalid date format. Please use DD/MM/YYYY format.
```

## Running Tests

Test classes use `main`-method-based assertions — no JUnit or other test framework is required.

### Compile Test Classes

```bash
javac -d out -cp out src/test/java/com/agecalculator/service/AgeCalculatorServiceTest.java \
                     src/test/java/com/agecalculator/util/DateValidatorTest.java
```

### Run Test Classes

```bash
java -cp out com.agecalculator.service.AgeCalculatorServiceTest
java -cp out com.agecalculator.util.DateValidatorTest
```

Each test prints a pass/fail result for every test case executed.

## Test Cases

### AgeCalculatorServiceTest

| Test Case | Description |
|-----------|-------------|
| Normal DOB | Validates age calculation for a standard past date (e.g., `15/08/1998`) |
| Leap Year DOB | Validates correct handling of a DOB on February 29 (e.g., `29/02/2000`) |
| Today's Date DOB | Validates zero-age result when DOB is today's date |
| Total Months | Validates total months computation via `ChronoUnit.MONTHS` |
| Total Days | Validates total days computation via `ChronoUnit.DAYS` |
| Next Birthday Countdown | Validates the countdown calculation to the next birthday occurrence |

### DateValidatorTest

| Test Case | Description |
|-----------|-------------|
| Valid Date | Validates successful parsing of a correctly formatted date (e.g., `15/08/1998`) |
| Invalid Calendar Date | Validates rejection of non-existent dates (e.g., `31/02/2020`) |
| Future Date | Validates rejection of a date in the future |
| Wrong Format | Validates rejection of incorrectly formatted input (e.g., `1998-08-15`) |
| Empty Input | Validates rejection of empty or blank strings |
| Leap Year Valid | Validates acceptance of `29/02/2000` (leap year) |
| Leap Year Invalid | Validates rejection of `29/02/2023` (non-leap year) |

## Technology Stack

| Component | Details |
|-----------|---------|
| **Language** | Java 17 (OpenJDK) |
| **Dependencies** | Java Standard Library only — zero external dependencies |
| **Key APIs** | `java.time.LocalDate`, `java.time.Period`, `java.time.format.DateTimeFormatter`, `java.time.temporal.ChronoUnit` |
| **Build** | Raw `javac` / `java` commands (no Maven or Gradle) |
| **Testing** | Main-method-based test classes with assertion-style checks |

## License

This project is provided as an educational exercise. No specific license has been applied.
