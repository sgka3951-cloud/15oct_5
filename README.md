# Age Calculator

A Java console application that calculates a user's exact age based on their Date of Birth (DOB). Built with the `java.time` API (`LocalDate`, `Period`, `DateTimeFormatter`), the application accepts a date of birth in `DD/MM/YYYY` format and outputs the result as:

```
Your age is X years, Y months, and Z days.
```

The project follows Object-Oriented design principles with cleanly separated concerns across model, service, utility, and exception layers.

## Prerequisites

- **Java Development Kit (JDK) 21** or higher
- **Apache Maven 3.8+**

Verify your installation by running:

```bash
java -version
mvn -version
```

## Project Structure

```
├── pom.xml
├── .gitignore
├── README.md
└── src/
    ├── main/java/com/agecalculator/
    │   ├── App.java
    │   ├── model/AgeResult.java
    │   ├── service/AgeCalculatorService.java
    │   ├── service/BirthdayCountdownService.java
    │   ├── util/DateValidator.java
    │   ├── exception/FutureDateException.java
    │   └── exception/InvalidDateFormatException.java
    └── test/java/com/agecalculator/
        ├── AppTest.java
        ├── service/AgeCalculatorServiceTest.java
        ├── service/BirthdayCountdownServiceTest.java
        └── util/DateValidatorTest.java
```

| Package | Responsibility |
|---------|---------------|
| `com.agecalculator` | Application entry point (`App.java`) |
| `com.agecalculator.model` | Immutable value object for age results (`AgeResult`) |
| `com.agecalculator.service` | Core business logic — age calculation and birthday countdown |
| `com.agecalculator.util` | Input validation and date parsing utilities |
| `com.agecalculator.exception` | Custom exceptions for invalid and future dates |

## Build Instructions

1. **Clone the repository:**

   ```bash
   git clone <repository-url>
   cd age-calculator
   ```

2. **Build the project:**

   ```bash
   mvn clean package
   ```

   This command compiles all source files, executes the full test suite, and packages the application into an executable JAR located at `target/age-calculator-1.0-SNAPSHOT.jar`.

## Run Instructions

After building, you can run the application using any of the following methods:

**Option 1 — Run from compiled classes:**

```bash
java -cp target/classes com.agecalculator.App
```

**Option 2 — Run the packaged JAR:**

```bash
java -jar target/age-calculator-1.0-SNAPSHOT.jar
```

## Usage Example

```
Enter your Date of Birth (DD/MM/YYYY): 15/08/1998
Your age is 27 years, 6 months, and 15 days.
```

> **Note:** The actual age values displayed will vary depending on the current system date at runtime.

### Error Handling Examples

**Invalid date (impossible calendar date):**

```
Enter your Date of Birth (DD/MM/YYYY): 31/02/2020
Error: Invalid date format. Please enter date in DD/MM/YYYY format.
```

**Future date:**

```
Enter your Date of Birth (DD/MM/YYYY): 25/12/2099
Error: Date of birth cannot be in the future: 25/12/2099
```

**Wrong format:**

```
Enter your Date of Birth (DD/MM/YYYY): 1998-08-15
Error: Invalid date format. Please enter date in DD/MM/YYYY format.
```

## Features

- **Exact age calculation** using `java.time.Period` — computes years, months, and days between the date of birth and the current date
- **Strict date validation** with `ResolverStyle.STRICT` — rejects impossible calendar dates such as `31/02/2020`
- **Custom exception handling** — dedicated `FutureDateException` and `InvalidDateFormatException` classes provide meaningful, user-friendly error messages
- **Countdown to next birthday** — enhancement service that calculates the number of days remaining until the user's next birthday
- **Object-Oriented design** — responsibilities are separated across model, service, utility, and exception packages following the Single Responsibility Principle

## Test Execution

Run the full test suite with:

```bash
mvn test
```

### Test Categories

| Category | Example Input | What Is Verified |
|----------|--------------|-----------------|
| Normal DOB calculation | `15/08/1998` | Correct age in years, months, and days |
| Leap year DOB | `29/02/2000` | Valid acceptance and correct age for leap year births |
| Invalid date rejection | `31/02/2020` | `InvalidDateFormatException` is thrown with a descriptive message |
| Future date rejection | Any date after today | `FutureDateException` is thrown with a descriptive message |
| Wrong format input | `1998-08-15`, empty string, non-date text | `InvalidDateFormatException` is thrown for malformed input |
| Birthday countdown | Various dates | Correct number of days until the user's next birthday |

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Programming language and runtime |
| Maven | 3.8+ | Build tool and dependency management |
| JUnit 5 | 5.14.3 | Unit testing framework |
| `java.time` API | JDK 21 (built-in) | Date/time operations — `LocalDate`, `Period`, `DateTimeFormatter` |
