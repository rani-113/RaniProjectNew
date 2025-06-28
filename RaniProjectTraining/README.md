# Cucumber Selenium Automation Framework

A simple and robust automation testing framework built with Cucumber, Selenium WebDriver, and TestNG using Maven.

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/automation/
│   │   │   ├── base/
│   │   │   │   └── BaseClass.java          # WebDriver management and common utilities
│   │   │   └── pages/
│   │   │       ├── LoginPage.java          # Page Object for Login functionality
│   │   │       └── RegistrationPage.java   # Page Object for Registration functionality
│   │   └── resources/
│   │       ├── webapp/
│   │       │   └── index.html              # Simple web application for testing
│   │       └── logback.xml                 # Logging configuration
│   └── test/
│       ├── java/com/automation/
│       │   ├── runners/
│       │   │   └── CucumberRunner.java     # Cucumber TestNG runner
│       │   └── steps/
│       │       ├── LoginSteps.java         # Step definitions for Login feature
│       │       └── RegistrationSteps.java  # Step definitions for Registration feature
│       └── resources/
│           └── features/
│               ├── Login.feature           # Login test scenarios
│               └── Registration.feature    # Registration test scenarios
├── pom.xml                                 # Maven configuration
├── testng.xml                             # TestNG configuration
└── README.md                              # Project documentation
```

## Features

- **Page Object Model (POM)**: Organized page objects for better maintainability
- **Cucumber BDD**: Behavior-driven development with Gherkin syntax
- **TestNG Integration**: Test execution and reporting
- **WebDriver Management**: Automatic WebDriver setup with WebDriverManager
- **Logging**: Comprehensive logging with SLF4J and Logback
- **Simple Web Application**: Built-in test application with login and registration forms

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser (WebDriverManager will handle driver installation)

## Setup Instructions

1. **Clone or download the project**
2. **Navigate to project directory**
   ```bash
   cd cucumber-selenium-framework
   ```

3. **Clean and compile the project**
   ```bash
   mvn clean compile
   ```

## Running Tests

### Run all tests using Maven
```bash
mvn clean test
```

### Run tests using TestNG XML
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run specific feature
```bash
mvn test -Dcucumber.options="--tags @login"
```

### Run specific scenario
```bash
mvn test -Dcucumber.options="--tags @positive"
```

## Test Scenarios

### Login Feature
- **Positive Test**: Successful login with valid credentials (username: testuser, password: password123)
- **Negative Test**: Failed login with invalid credentials

### Registration Feature
- **Positive Test**: Successful registration with valid data
- **Negative Test**: Failed registration with empty fields

## Test Reports

After test execution, reports will be generated in:
- **HTML Report**: `target/cucumber-reports/cucumber-pretty.html`
- **JSON Report**: `target/cucumber-reports/CucumberTestReport.json`
- **JUnit XML Report**: `target/cucumber-reports/CucumberTestReport.xml`
- **Logs**: `target/logs/automation.log`

## Web Application

The framework includes a simple web application (`src/main/resources/webapp/index.html`) with:
- Login form with username/password fields
- Registration form with name, email, username, and password fields
- Tab navigation between forms
- Client-side validation and success/error messages

## Framework Components

### BaseClass
- WebDriver initialization and management
- Common utility methods
- Exception handling

### Page Objects
- **LoginPage**: Handles login form interactions
- **RegistrationPage**: Handles registration form interactions
- Implements Page Object Model pattern

### Step Definitions
- **LoginSteps**: Implements login feature steps
- **RegistrationSteps**: Implements registration feature steps
- Includes proper setup and teardown methods

### Cucumber Runner
- TestNG integration
- Multiple report formats
- Configurable test execution

## Best Practices Implemented

1. **Page Object Model**: Separates test logic from page interactions
2. **Explicit Waits**: Uses WebDriverWait for reliable element interactions
3. **Exception Handling**: Comprehensive error handling and logging
4. **Logging**: Detailed logging for debugging and monitoring
5. **Clean Architecture**: Well-organized package structure
6. **Reusable Components**: Common utilities in BaseClass

## Customization

### Adding New Features
1. Create feature file in `src/test/resources/features/`
2. Create step definitions in `src/test/java/com/automation/steps/`
3. Create page objects in `src/main/java/com/automation/pages/`
4. Update CucumberRunner if needed

### Configuration
- Modify `pom.xml` for dependencies
- Update `testng.xml` for test execution
- Configure `logback.xml` for logging preferences

## Troubleshooting

### Common Issues
1. **WebDriver Issues**: Ensure Chrome browser is installed
2. **Path Issues**: Verify HTML file path in step definitions
3. **Dependency Issues**: Run `mvn clean install` to resolve dependencies

### Debug Mode
Enable debug logging by modifying `logback.xml`:
```xml
<logger name="com.automation" level="DEBUG"/>
```

## Contributing

1. Follow the existing code structure
2. Add proper logging and exception handling
3. Update documentation for new features
4. Ensure all tests pass before submitting

## License

This project is for educational and training purposes. 