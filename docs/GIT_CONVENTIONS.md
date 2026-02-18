# :book: Git Commit Convention

This project follows the **Conventional Commits** specification to ensure
clear history, maintainability, and structured collaboration.

---

<h1>Table of contents</h1>

- [:open_file_folder: 1. Commit Message Format](#open_file_folder-1-commit-message-format)
- [:label: 2. Types](#label-2-types)
- [:card_file_box: 3. Scope Guidelines](#card_file_box-3-scope-guidelines)
    - [:jigsaw: 3.1 Core Modules](#jigsaw-31-core-modules)
    - [:building_construction: 3.2 Infrastructure](#building_construction-32-infrastructure)
    - [:books: 3.3 Documentation](#books-33-documentation)
    - [:test_tube: 3.4 Testing](#test_tube-34-testing)
- [:pencil2: 4. Writing Good Commit Messages](#pencil2-4-writing-good-commit-messages)
- [:balance_scale: 5. When to Use feat vs refactor](#balance_scale-5-when-to-use-feat-vs-refactor)
- [:white_check_mark: 6. Good Examples](#white_check_mark-6-good-examples)
- [:x: 7. Bad Examples](#x-7-bad-examples)
- [:rocket: 8. Why We Use This Convention](#rocket-8-why-we-use-this-convention)

---

## :open_file_folder: 1. Commit Message Format

Each commit message must follow this structure:

```
<type>(<scope>): <description>
```

Example:

```
feat(core-browser): add browser interface
```

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :label: 2. Types

* **feat**: introduce a new feature
* **fix**: fix a bug
* **refactor**: internal code improvement (no feature or bug fix)
* **docs**: documentation changes
* **style**: formatting changes (no logic impact)
* **test**: add or update tests
* **chore**: maintenance tasks, dependency updates, build configuration
* **perf**: performance improvements
* **ci**: continuous integration changes

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :card_file_box: 3. Scope Guidelines

The scope indicates the module or area affected by the change.

### :jigsaw: 3.1 Core Modules

* `core-browser`
* `core-driver`
* `core-config`

### :building_construction: 3.2 Infrastructure

* `build`
* `dependencies`
* `ci`

### :books: 3.3 Documentation

* `readme`
* `conventions`
* `standards`

### :test_tube: 3.4 Testing

* `tests`
* `hooks`
* `steps`

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :pencil2: 4. Writing Good Commit Messages

- Use imperative mood
- Keep description concise and clear
- Do not capitalize the first letter of the description
- Capitalize proper nouns, acronyms, and technical identifiers (e.g., DriverManager, WebDriverWait, API, CI)
- Do not end with a period
- Keep the message under 72 characters when possible


<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :balance_scale: 5. When to Use feat vs refactor

* Use **feat** when introducing new functionality or architectural components
* Use **refactor** when improving internal structure without changing behavior

Example:

```
feat(core-driver): implement DriverFactory with strategy pattern
refactor(core-driver): simplify driver initialization logic
```

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :white_check_mark: 6. Good Examples

```
feat(core-driver): implement DriverManager singleton with WebDriverWait
feat(core-browser): add Browser interface
fix(core-config): resolve properties loading issue
docs(readme): update architecture overview
chore(build): update Selenium dependency
ci(build): configure GitHub Actions pipeline
```

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :x: 7. Bad Examples

```
Added driver manager
Fixed bug.
Changes
update stuff
```

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

---

## :rocket: 8. Why We Use This Convention

* Improves commit history readability
* Makes project evolution easier to track
* Enables automated changelog generation based on commit history
* Encourages modular thinking
* Maintains consistency across contributors

<div align="right">
  <strong>
    <a href="#table-of-contents" style="text-decoration: none;">↥ Back to top</a>
  </strong>
</div>

<div align="left">
  <b><a href="../README.md#table-of-contents">↥ Back to main page</a></b>
</div>
