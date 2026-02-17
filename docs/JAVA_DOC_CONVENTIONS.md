# Documentation Conventions

Simple rules to keep documentation clear and consistent.

## General Rules

- Keep descriptions short.
- Explain WHY when needed, not obvious code.
- Be consistent across all classes.

## Class Documentation

Include:

- Main responsibility
- Important behavior
- Design pattern (if used)

## Method Documentation

Document methods when:

- Logic is not obvious
- Method has side effects
- Part of framework core

## @param and @return Style

- Short descriptions
- No period at the end
- Lowercase text

Example:

@param browserType browser type to initialize  
@return WebDriver instance

## Enum Documentation

Explain purpose and avoid magic strings.

## Naming Rules

Use clear names:

- config
- driver
- browser

Avoid unnecessary abbreviations.

## {@inheritDoc} — When to use

Use `{@inheritDoc}` when:

- The method overrides or implements a parent method.
- The parent already has complete JavaDoc.
- You do NOT need to repeat the same documentation.

Use `{@inheritDoc}` + extra text when:

- You want to add implementation details or special behavior.

Do NOT use when:

- The method behavior is different from the parent.
- The parent has no useful documentation.

### Example

```java
  /**
   * {@inheritDoc}
   */
  @Override
  public WebDriver getBrowser() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver();
  }
```