# :rocket: Selenium Automation Framework

Automation testing framework built using **Java**, **Selenium**,
**TestNG**, **Cucumber**, **Gradle**, **Allure**, and **GitHub
Actions**.

This project demonstrates how to build a scalable, maintainable, and
CI-ready UI automation framework from scratch using clean architecture,
clear separation of responsibilities, and well-known design patterns.

---

<h1>Table of contents</h1>

-   [:open_file_folder: 1. Project](#open_file_folder-1-project)
-   [:card_file_box: 2. Project
    Structure](#card_file_box-2-project-structure)
-   [:gear: 3. Design Patterns](#gear-3-design-patterns)
-   [:balance_scale: 4. Design Principles
    Applied](#balance_scale-4-design-principles-applied)
-   [:wrench: 5. Configuration
    Management](#wrench-5-configuration-management)
-   [:computer: 6. Installation &
    Execution](#computer-6-installation--execution)
-   [:bar_chart: 7. Reporting (Allure)](#bar_chart-7-reporting-allure)
-   [:white_check_mark: 8. Static Code
    Analysis](#white_check_mark-8-static-code-analysis)
-   [:rocket: 9. CI/CD Enhancements](#rocket-9-cicd-enhancements)
-   [:books: 10. Documentation Standards](#books-10-documentation-standards)
-   [:sparkles: 11. Key Features](#sparkles-11-key-features)

---

## :open_file_folder: 1. Project

### :bookmark: Introduction

This framework evolved from a basic Selenium project into an
enterprise-ready automation framework  including:

-   CI/CD integration
-   Static code analysis
-   Strict Javadoc validation
-   Allure reporting with history
-   Re-run failed scenarios
-   Thread-safe driver management
-   Headless execution support

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :card_file_box: 2. Project Structure

``` mermaid
flowchart TD

subgraph Core
    subgraph Browser
        BrowserInterface
        Chrome
        Firefox
        Edge
        ChromeHeadless
    end

    subgraph Driver
        DriverFactory
        DriverManager
        DriverType
    end

    subgraph Config
        PropertiesManager
        PropertiesInput
    end

    subgraph Utilities
        ExplicitWait
        WebElementActions
    end
end
```

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :gear: 3. Design Patterns

### :repeat: Strategy Pattern

Each browser implements a common interface:
``` java
public interface Browser {
    WebDriver getBrowser();
}
```

Enables browser extension without modifying existing code.

Example implementation:

``` java
public class Chrome implements Browser {

  @Override
  public WebDriver getBrowser() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver();
  }
}
```

Benefits:

-   Easy browser extension
-   Open/Closed Principle applied
-   Decoupled browser logic

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### :factory: Factory Pattern (DriverFactory)

Driver creation is centralized:

``` java
public static WebDriver getDriverManager(final DriverType type) {
    Browser browser = BROWSERS.get(type);
    return browser.getBrowser();
}
```

Benefits:

- Single place for driver instantiation
- Reduced coupling
- Easier maintenance

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### :lock: Thread-Safe Driver Management (ThreadLocal Pattern)

Driver lifecycle is managed using `ThreadLocal` to guarantee one isolated `WebDriver` instance per execution thread.

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

public static void setDriver(WebDriver driver) {
    DRIVER.set(driver);
}

public static WebDriver getDriver() {
    return DRIVER.get();
}

public static void quitDriver() {
    DRIVER.get().quit();
    DRIVER.remove();
}
```

Benefits:

-   True parallel execution support
-   No shared state between tests
-   Improved stability in CI environments
-   Proper memory cleanup with remove()
-   Scalable and enterprise-ready execution model

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### :page_facing_up: Page Object Model

``` java
public abstract class BasePage {

    protected WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }
}
```

Encapsulates UI logic and improves maintainability.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### :recycle: Reusable Actions Layer

Custom abstraction over WebElement:

``` java
public void click(WebElement element) {
    wait.until(ExpectedConditions.elementToBeClickable(element));
    element.click();
    log.info("Clicked element: {}", element);
}
```

Benefits:

-   Centralized logging
-   Automatic waits
-   Reduced test duplication
-   Cleaner step definitions

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :balance_scale: 4. Design Principles Applied

### ✅ Single Responsibility Principle (SRP)

Each class has one responsibility:

-   DriverManager → Driver lifecycle
-   PropertiesManager → Configuration
-   WebElementActions → UI interactions

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### ✅ Open/Closed Principle (OCP)

New browser support:

``` java
BROWSERS.put(DriverType.SAFARI, new Safari());
```

No modification required in existing logic.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### ✅ Dependency Inversion Principle (DIP)

High-level modules depend on abstractions:

``` java
private final Browser browser;
```

Instead of concrete implementations.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### ✅ Separation of Concerns

-   Test logic → Step Definitions
-   UI logic → Page Objects
-   Browser logic → Strategy layer
-   Config → Properties layer
-   CI → Pipeline layer

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### ✅ Clean Architecture

Layered approach:

-   Core (Driver, Config)
-   Utilities (Actions, Waits)
-   Pages (UI representation)
-   Tests (BDD layer)
-   CI (Automation pipeline)

Dependencies flow inward only.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### ✅ Thread-Safe Driver Management

Parallel-safe execution using ThreadLocal.

Prevents cross-thread contamination and flaky tests.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :wrench: 5. Configuration Management

### :key: 1. PropertiesInput Enum

```java
public enum PropertiesInput {
  BROWSER("browser"),
  BASE_URL("baseUrl"),
  EXPLICIT_WAIT("explicitWait");
}
```

### :clipboard: 2. PropertiesManager

```java
public String getProperty(PropertiesInput key) {
  String systemValue = System.getProperty(key.getPropertiesName());
  return systemValue != null ? systemValue : properties.getProperty(key.getPropertiesName());
}
```

This allows configuration to be read from:

- Command-line parameters
- `config.properties`

### :computer: 3. Command-Line Execution Example

Run tests with a specific browser:

```bash
./gradlew executeFeatures -Dbrowser=CHROME
```

If no parameter is provided, the value defined in `config.properties` will be used.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :computer: 6. Installation & Execution

Always use Gradle Wrapper:

``` bash
./gradlew clean executeFeatures
```

Run by tag:

``` bash
./gradlew clean executeFeatures -Dcucumber.filter.tags='@regression'
```

Re-run failed scenarios:

``` bash
./gradlew reExecuteFeatures
```

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :bar_chart: 7. Reporting (Allure)

Generate report:

``` bash
./gradlew allureServe
```

Features:

-   Screenshot on failure
-   Execution time
-   History tracking
-   CI artifact publishing

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :white_check_mark: 8. Static Code Analysis

``` bash
./gradlew staticAnalysis
```

Includes:

- Checkstyle (Google Java Style Guide)
- PMD rule enforcement
- Javadoc validation

Build fails on violations.

### :memo: Javadoc Validation

```bash
./gradlew javadoc
```

Javadoc documentation is strictly enforced to maintain professional code standards.

Build fails if:

- Public classes are not documented
- Public methods are missing Javadoc
- Tags such as `@param` or `@return` are incomplete or incorrect

Ensures consistent documentation, improved readability, and long-term maintainability across the framework.

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :rocket: 9. CI/CD Enhancements

GitHub Actions pipeline:

-   Checkout
-   Gradle build
-   Static analysis
-   Javadoc validation
-   Parallel test execution strategy
-   Allure result generation
-   Artifact upload
-   Deploy to GitHub Pages
-   Scheduled nightly execution (cron)
-   Workflow badge in README

CI Highlights:

-   Fails fast on quality violations
-   Historical report tracking
-   Fully automated deployment
-   Parallel-ready pipeline

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :books: 10. Documentation Standards

Project documentation is centralized in the `/docs` folder to keep the README clean and focused.

### :scroll: 1. Git Commit Conventions

Commit message standards based on **Conventional Commits** are defined here:

➡️ [`docs/GIT_CONVENTIONS.md`](docs/GIT_CONVENTIONS.md)

This document explains:
- Commit message format
- Allowed types and scopes
- Good and bad examples
- Why these conventions are used

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

### :memo: 2. JavaDoc Conventions

Java documentation standards are defined here:

➡️ [`docs/JAVADOC_CONVENTIONS.md`](docs/JAVADOC_CONVENTIONS.md)

This document explains:
- How to document classes, methods, and enums
- When to use `{@inheritDoc}`
- Naming rules and documentation best practices

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---

## :sparkles: 11. Key Features

-   Clean layered architecture
-   SOLID principles applied
-   Strategy + Factory + ThreadLocal Driver Management Pattern
-   Composite Gradle pipeline
-   Reusable Actions abstraction
-   Thread-safe parallel execution
-   Headless browser support
-   Re-run failed scenarios
-   Strict static analysis enforcement
-   Allure reporting with history
-   Automated CI/CD with deployment

<div style="text-align: right;">
  <strong>
    <a href="#table-of-contents">↥ Back to top</a>
  </strong>
</div>

---
