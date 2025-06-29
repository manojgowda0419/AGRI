# Agrichain Automation Framework

A simple test automation framework for the Agrichain application using Selenium WebDriver and TestNG.

## Prerequisites

Make sure you have these installed:

1. **Java 11+**
   ```bash
   java -version
   ```

2. **Maven**
   ```bash
   mvn -version
   ```

3. **Chrome Browser** (latest version recommended)

## Quick Setup

1. **Clone the repo**
   ```bash
   git clone <repository-url>
   cd automation-framework
   ```

2. **Update config file**
   Edit `src/main/resources/config.properties` and update the file paths:
   ```properties
   # Update these paths to match your system
   base.url=file:///YOUR_PATH/index.html
   home.url=file:///YOUR_PATH/home.html
   result.url=file:///YOUR_PATH/result.html
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

## Running Individual Tests

```bash
# Run specific test
mvn test -Dtest=AgrichainE2ETest#testCompleteUserJourney

# Run with different browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true
```

## Test Coverage

- **E2E User Journey** - Complete flow from login to logout
- **Algorithm Testing** - String processing validation
- **Authentication Flow** - Login/logout testing
- **UI Navigation** - Page navigation and element validation

## Project Structure

```
src/
├── main/java/
│   ├── config/         # Configuration management
│   ├── pages/          # Page Object Model classes
│   └── utils/          # Utility classes
├── test/java/tests/    # Test classes
└── resources/
    ├── config.properties
    ├── log4j2.xml
    └── testdata/
```

## Configuration

Key settings in `config.properties`:
- `browser` - Browser to use (chrome, firefox, edge)
- `headless` - Run in headless mode (true/false)
- `test.email` - Test user email
- `test.password` - Test user password

## Test Data

Test cases and expected results are defined in:
- `src/test/resources/testdata/testdata.json`
- Hardcoded in test classes for simplicity

## Reports

Test reports are generated in:
- `target/surefire-reports/` - TestNG HTML reports
- `logs/` - Application logs

## Troubleshooting

**Common Issues:**

1. **ChromeDriver issues** - Make sure Chrome is updated
2. **File path errors** - Update URLs in config.properties
3. **Port conflicts** - No server needed, uses file:// URLs

**Debug mode:**
```bash
mvn test -X -Dtest=AgrichainE2ETest#testCompleteUserJourney
```

## Notes

- Tests use file:// URLs for local HTML files
- No authentication validation - any email/password works
- Framework designed for demo purposes
- Add more test cases as needed

## TODO

- [ ] Add cross-browser parallel execution
- [ ] Implement screenshot on failure
- [ ] Add performance benchmarking
- [ ] Mobile responsive testing
- [ ] API testing integration

---

**Author:** MANOJ K S
**Version:** 1.0  
**Last Updated:** June 2025 