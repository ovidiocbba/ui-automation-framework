# :books: JavaDoc Conventions

Simple rules to keep documentation clear and consistent.

---

<h1>Table of contents</h1>

- [:page_facing_up: 1. General Rules](#page_facing_up-1-general-rules)
- [:classical_building: 2. Class Documentation](#classical_building-2-class-documentation)
- [:gear: 3. Method Documentation](#gear-3-method-documentation)
- [:label: 4. @param and @return Style](#label-4-param-and-return-style)
- [:bookmark_tabs: 5. Enum Documentation](#bookmark_tabs-5-enum-documentation)
- [:abc: 6. Naming Rules](#abc-6-naming-rules)
- [:repeat: 7. {@inheritDoc} — When to use](#repeat-7-inheritdoc--when-to-use)

---

## :page_facing_up: 1. General Rules

* Keep descriptions short.
* Explain WHY when needed, not obvious code.
* Be consistent across all classes.

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :classical_building: 2. Class Documentation

Include:

* Main responsibility
* Important behavior
* Design pattern (if used)

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :gear: 3. Method Documentation

Document methods when:

* Logic is not obvious
* Method has side effects
* Part of framework core

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :label: 4. @param and @return Style

* Short descriptions
* No period at the end
* Lowercase text

Example:
```
@param browserType browser type to initialize
@return WebDriver instance
```

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :bookmark_tabs: 5. Enum Documentation

Explain purpose and avoid magic strings.

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :abc: 6. Naming Rules

Use clear names:

* config
* driver
* browser

Avoid unnecessary abbreviations.

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :repeat: 7. {@inheritDoc} — When to use

Use `{@inheritDoc}` when:

* The method overrides or implements a parent method.
* The parent already has complete JavaDoc.
* You do NOT need to repeat the same documentation.

Use `{@inheritDoc}` + extra text when:

* You want to add implementation details or special behavior.

Do NOT use when:

* The method behavior is different from the parent.
* The parent has no useful documentation.

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

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

<div align="left">
  <b><a href="../README.md#table-of-contents">↥ Back to main page</a></b>
</div>
