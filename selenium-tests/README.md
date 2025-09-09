# VAgent Selenium Test Automation

This project contains automated tests for the VAgent application using Selenium WebDriver with Java and TestNG.

## Project Structure

```
selenium-tests/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── vagent/
│   │               ├── config/          # Configuration management
│   │               ├── driver/          # WebDriver factory
│   │               ├── pages/           # Page Object Models
│   │               └── utils/           # Utility classes
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── vagent/
│       │           └── tests/           # Test classes
│       └── resources/                   # Configuration files
├── test-output/                         # Test reports and screenshots
└── pom.xml                             # Maven dependencies
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome/Firefox/Edge browser installed
- VAgent application running on https://vagentbeta.onrender.com (production)
- Backend API running on https://vagentbetabackend.onrender.com

## Setup Instructions

1. **Install Java and Maven**
   - Download and install Java 11+ from Oracle or OpenJDK
   - Download and install Maven from https://maven.apache.org/

2. **Clone or download the test project**
   ```bash
   cd selenium-tests
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

4. **Configure test settings**
   - Edit `src/test/resources/config.properties` to modify:
     - Base URL (default: https://vagentbeta.onrender.com)
     - Browser choice (chrome, firefox, edge)
     - Timeout settings

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
# Run smoke tests only
mvn test -DsuiteXmlFile=src/test/resources/smoke-tests.xml

# Run full test suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthenticationTest
mvn test -Dtest=ProjectManagementTest
mvn test -Dtest=CollaborationTest
mvn test -Dtest=UIElementsTest
```

### Run with Different Browser
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Run in Headless Mode
Edit `src/main/java/com/vagent/driver/DriverFactory.java` and uncomment the headless options for your browser.

## Test Suites

### 1. Authentication Tests (`AuthenticationTest.java`)
- Login page elements verification
- User registration functionality
- User login functionality
- Navigation between login/register pages
- Logout functionality
- Form validation

### 2. Project Management Tests (`ProjectManagementTest.java`)
- Create new projects
- Edit project names
- Delete projects
- Project persistence
- Multiple project handling
- Form validation for project creation

### 3. Collaboration Tests (`CollaborationTest.java`)
- Project messaging system
- Collaborator panel functionality
- Message persistence
- Multi-user session handling
- Project access controls

### 4. UI Elements Tests (`UIElementsTest.java`)
- Page layout verification
- Responsive design testing
- Button interactions
- Modal behavior
- Browser navigation
- Page refresh handling

## Page Object Model

The tests use Page Object Model (POM) design pattern with the following pages:

- **BasePage**: Common functionality for all pages
- **LoginPage**: Login form interactions
- **RegisterPage**: Registration form interactions
- **HomePage**: Dashboard with project management
- **ProjectPage**: Individual project workspace

## Configuration

### Browser Configuration
Edit `src/test/resources/config.properties`:
```properties
browser=chrome          # Options: chrome, firefox, edge
base.url=https://vagentbeta.onrender.com
implicit.wait=15
explicit.wait=30
```

### Test Data
The framework includes automatic test data generation for:
- Random email addresses
- Random passwords
- Random project names
- Test messages

## Reports and Screenshots

- **Test Reports**: Generated in `test-output/` directory
- **Screenshots**: Automatically captured on test failures in `test-output/screenshots/`
- **ExtentReports**: HTML reports with detailed test execution information

## Best Practices

1. **Page Object Model**: Each page has its own class with locators and methods
2. **Data Generation**: Use TestDataGenerator for creating unique test data
3. **Waits**: Explicit waits are used for reliable element interactions
4. **Screenshots**: Automatic screenshot capture on test failures
5. **Configuration**: Externalized configuration for easy environment switching

## Troubleshooting

### Common Issues

1. **WebDriver not found**
   - Solution: WebDriverManager automatically downloads drivers, ensure internet connection

2. **Element not found**
   - Solution: Check if application is running on correct URL
   - Verify element locators in page objects

3. **Tests timing out**
   - Solution: Increase timeout values in config.properties
   - Check if application is responding correctly

4. **Browser not opening**
   - Solution: Ensure browser is installed and accessible
   - Try running with different browser

### Debug Mode
To run tests in debug mode with detailed logging:
```bash
mvn test -X
```

## Maintenance

1. **Update Dependencies**: Regularly update Maven dependencies in `pom.xml`
2. **Page Objects**: Update locators when UI changes
3. **Test Data**: Ensure test data generators create valid data
4. **Browser Compatibility**: Test with different browser versions

## Contributing

1. Follow existing code structure and naming conventions
2. Add appropriate comments and documentation
3. Ensure all tests pass before committing
4. Update this README when adding new features

## Contact

For questions or issues, please contact the development team.
