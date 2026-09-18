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
|   |-- pages         # Page Object Model classes
|   `-- utils         # Shared utilities
`-- test
    |-- java/com/example/automation
    |   |-- hooks             # Cucumber lifecycle hooks
    |   |-- runners           # TestNG Cucumber runner
    |   `-- stepdefinitions   # Step definitions
    `-- resources
        |-- config/application.properties
        |-- features/smoke.feature, hello-world.feature
        `-- suites/testng.xml

apps
|-- backend   # Spring Boot hello world API
`-- frontend  # React (Vite) hello world UI
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
| `baseUrl.<env>` | `baseUrl.qa=https://example.com` | Environment URL |
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
- `.github/workflows/ci-cd.yml`
- `Jenkinsfile`

## Reports

Reports are generated after every run:

```text
target/cucumber-reports/cucumber.html
target/cucumber-reports/cucumber.json
target/cucumber-reports/cucumber.xml
```

## Hello World Application

`apps/` contains a small full-stack application used as the automation target:

- `apps/backend`: Spring Boot 3 service exposing `GET /api/hello` and `/actuator/health`
- `apps/frontend`: React 18 (Vite) UI that renders the greeting and can refresh it; nginx proxies `/api` to the backend

Run the whole stack with Docker:

```bash
docker compose up -d --build
# frontend http://localhost:3000, backend http://localhost:8080/api/hello
```

Run the e2e suite against it:

```bash
mvn clean test -Denv=local -Dheadless=true -Dcucumber.filter.tags="@e2e"
```

Run the pieces without Docker:

```bash
cd apps/backend && mvn spring-boot:run
cd apps/frontend && npm install && npm run dev   # http://localhost:5173, proxies /api to :8080
```

## Automated Pipeline

`.github/workflows/ci-cd.yml` runs on every push and pull request:

1. `unit-tests`: backend `mvn verify` and frontend `npm ci && npm run build`
2. `e2e-tests`: `docker compose up -d --build`, then the `@e2e` Selenium/Cucumber suite against the containers, with the Cucumber report uploaded as an artifact
3. `publish-images`: on `main`, builds and pushes `hello-backend` and `hello-frontend` images to GHCR tagged with the short SHA and `latest`
4. `deploy-prod`: deploys those tags to the production host over SSH with `docker-compose.prod.yml` (uses the `production` GitHub environment)
5. `smoke-prod`: re-runs the `@e2e` suite against `PROD_BASE_URL`

Deployment configuration (repository settings):

| Name | Type | Purpose |
| --- | --- | --- |
| `PROD_SSH_HOST` | secret | Production host; deploy step is skipped when unset |
| `PROD_SSH_USER` | secret | SSH user |
| `PROD_SSH_KEY` | secret | SSH private key |
| `PROD_APP_DIR` | secret | Directory on the host holding `docker-compose.prod.yml` |
| `PROD_BASE_URL` | variable | Public URL used by the post-deploy smoke tests |

## Current Sample Test

The framework intentionally has only one Cucumber scenario:

```gherkin
Scenario: Open browser and print hello
  Given I open the configured application
  Then I print hello
```

This keeps the test case minimal while the automation infrastructure remains ready for real project test coverage.
