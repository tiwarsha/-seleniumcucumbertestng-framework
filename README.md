# Selenium Cucumber TestNG Framework

Modern Java Selenium framework focused on reusable automation infrastructure:

- Selenium 4 with Java 17
- Cucumber BDD with TestNG runner
- Page Object Model structure
- Thread-safe WebDriver using `ThreadLocal`
- Chrome, Firefox, and Edge support
- Local or Selenium Grid execution
- Parallel scenario execution through TestNG `@DataProvider(parallel = true)`
- Multi-environment configuration through properties, JVM arguments, or environment variables
- CI/CD ready Maven commands and GitHub Actions sample
- Jenkins pipeline and Selenium Grid Docker Compose samples
- Cucumber HTML, JSON, and JUnit reports

## Project Structure

```text
src
|-- main/java/com/example/automation
|   |-- config        # Runtime config resolver
|   |-- driver        # Driver factory and ThreadLocal manager
|   |-- model         # Test data models
|   |-- pages         # Page Object Model classes
|   `-- utils         # Shared utilities
`-- test
    |-- java/com/example/automation
    |   |-- hooks             # Cucumber lifecycle hooks
    |   |-- runners           # TestNG Cucumber runner
    |   `-- stepdefinitions   # Step definitions
    `-- resources
        |-- config/application.properties
        |-- features/smoke.feature
        |-- features/end_to_end_purchase.feature
        `-- suites/testng.xml
```

## End To End Scenario

`end_to_end_purchase.feature` covers the shopping flow on https://askomdch.com:
store search, add to cart, cart verification, quantity update, checkout with billing
details, direct bank transfer payment, and order confirmation. Feature file and step
definitions are kept separate: steps live in
`src/test/java/com/example/automation/stepdefinitions/EndToEndPurchaseSteps.java`
and all locators live in the page objects under `src/main/java/com/example/automation/pages`.

```bash
mvn clean test -Dheadless=true -Dcucumber.filter.tags="@e2e"
```

## Run Tests

Default local Chrome run:

```bash
mvn clean test
```

Headless QA smoke run:

```bash
mvn clean test -Dbrowser=chrome -Denv=qa -Dheadless=true -Dcucumber.filter.tags="@smoke"
```

Firefox against stage:

```bash
mvn clean test -Dbrowser=firefox -Denv=stage -Dheadless=true
```

Remote Selenium Grid:

```bash
mvn clean test -Dremote=true -DgridUrl=http://localhost:4444/wd/hub -Dbrowser=chrome -Denv=qa
```

Start a local Selenium Grid:

```bash
docker compose -f docker-compose.selenium-grid.yml up -d
```

## Configuration Priority

Runtime values are resolved in this order:

1. JVM system property, for example `-Dbrowser=edge`
2. Environment variable, for example `BROWSER=edge`
3. `src/test/resources/config/application.properties`
4. Framework default

Supported keys:

| Key | Example | Purpose |
| --- | --- | --- |
| `browser` | `chrome`, `firefox`, `edge` | Browser selection |
| `env` | `dev`, `qa`, `stage`, `prod` | Environment selection |
| `baseUrl.<env>` | `baseUrl.qa=https://askomdch.com` | Environment URL |
| `headless` | `true` | CI-friendly headless run |
| `remote` | `true` | Use Selenium Grid |
| `gridUrl` | `http://localhost:4444/wd/hub` | Grid endpoint |
| `explicitWaitSeconds` | `10` | Explicit wait timeout |
| `pageLoadTimeoutSeconds` | `30` | Page load timeout |

## Parallel Execution

Parallel execution is enabled in two places:

- `TestRunner.scenarios()` uses `@DataProvider(parallel = true)`
- `src/test/resources/suites/testng.xml` sets suite-level parallel execution

Because WebDriver is stored in a `ThreadLocal`, each scenario gets its own isolated browser instance.

You can tune TestNG data provider threads from Maven:

```bash
mvn clean test -DthreadCount=4 -Dheadless=true
```

## CI/CD Integration

Use the same Maven command in any CI/CD tool:

```bash
mvn clean test -Dbrowser=chrome -Denv=qa -Dheadless=true -Dcucumber.filter.tags="@smoke"
```

Included examples:

- `.github/workflows/selenium-tests.yml`
- `Jenkinsfile`

## Reports

Reports are generated after every run:

```text
target/cucumber-reports/cucumber.html
target/cucumber-reports/cucumber.json
target/cucumber-reports/cucumber.xml
```

## Current Tests

```gherkin
@smoke
Scenario: Open browser and print hello

@e2e @regression
Scenario Outline: Search a product, add it to the cart and place the order
```
