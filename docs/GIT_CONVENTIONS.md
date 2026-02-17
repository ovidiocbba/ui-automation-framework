# Git Commit Convention

This project follows the **Conventional Commits** specification to ensure
clear history, maintainability, and structured collaboration.

## Commit Message Format

Each commit message must follow this structure:

    <type>(<scope>): <description>

Example:

    feat(core-browser): add browser interface

## Types

- **feat**: introduce a new feature
- **fix**: fix a bug
- **refactor**: internal code improvement (no feature or bug fix)
- **docs**: documentation changes
- **style**: formatting changes (no logic impact)
- **test**: add or update tests
- **chore**: maintenance tasks, dependency updates, build configuration
- **perf**: performance improvements
- **ci**: continuous integration changes

## Scope Guidelines

The scope indicates the module or area affected by the change.

### Core Modules
- `core-browser`
- `core-driver`
- `core-config`

### Infrastructure
- `build`
- `dependencies`
- `ci`

### Documentation
- `readme`
- `conventions`
- `standards`

### Testing
- `tests`
- `hooks`
- `steps`

## Writing Good Commit Messages

✔ Use imperative mood  
✔ Keep description concise and clear  
✔ Do not capitalize the first letter of the description
✔ Capitalize proper nouns, acronyms, and technical identifiers (e.g., DriverManager, WebDriverWait, API, CI)
✔ Do not end with a period
✔ Keep the message under 72 characters when possible

## When to Use feat vs refactor

- Use **feat** when introducing new functionality or architectural components
- Use **refactor** when improving internal structure without changing behavior

Example: 
```
feat(core-driver): implement DriverFactory with strategy pattern
refactor(core-driver): simplify driver initialization logic
```

## Good examples:

```
feat(core-driver): implement DriverManager singleton with WebDriverWait
feat(core-browser): add Browser interface
fix(core-config): resolve properties loading issue
docs(readme): update architecture overview
chore(build): update Selenium dependency
ci(build): configure GitHub Actions pipeline
```

## Bad examples:
```
Added driver manager
Fixed bug.
Changes
update stuff
```

## Why We Use This Convention

- Improves commit history readability
- Makes project evolution easier to track
- Enables automated changelog generation based on commit history
- Encourages modular thinking
- Maintains consistency across contributors
