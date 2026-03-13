## 🔧 GitHub Actions Configuration

This project requires specific **GitHub Actions variables** to run the
CI workflows correctly.

------------------------------------------------------------------------

### 📍 How to Configure

Navigate to:

Repository → Settings → Secrets and variables → Actions

------------------------------------------------------------------------

## 🔹 Repository Variables

Click on **Variables → New repository variable** and add the following:

| Name                 | Example Value                                            | Description                         |
|----------------------|----------------------------------------------------------|-------------------------------------|
| `BASE_URL`           | `https://www.amazon.com`                                 | Base URL used during test execution |
| `EXPLICIT_WAIT`      | `30`                                                     | Explicit wait timeout (in seconds)  |
| `SCENARIO_TAG`       | `@regression`                                            | Default Cucumber tag for execution  |
| `SUPPORTED_BROWSERS` | `["CHROME_HEADLESS","FIREFOX_HEADLESS","EDGE_HEADLESS"]` | List of supported browsers used     |

  ------------------------------------------------------------------------------------------------------------------

### 📘 SUPPORTED_BROWSERS Variable

The `SUPPORTED_BROWSERS` repository variable defines the list of
browsers that the GitHub Actions workflow can execute.

Example value:

`["CHROME_HEADLESS","FIREFOX_HEADLESS","EDGE_HEADLESS"]`

This variable is used to dynamically generate the execution matrix when
the user selects **ALL** in the manual workflow.

Example configuration in the workflow:

``` yaml
strategy:
  matrix:
    # Browser list comes from repository variable SUPPORTED_BROWSERS (vars.SUPPORTED_BROWSERS)
    # To update it go to: Settings → Secrets and variables → Actions → Variables
    browser: ${{ inputs.browser == 'ALL' && fromJson(vars.SUPPORTED_BROWSERS) || fromJson(format('["{0}"]', inputs.browser)) }}
```

### How It Works

1. If the user selects:

`ALL`

The workflow loads the browser list from the repository variable:

`vars.SUPPORTED_BROWSERS`

Which expands into:

`["CHROME_HEADLESS","FIREFOX_HEADLESS","EDGE_HEADLESS"]`

GitHub Actions then runs one job per browser in parallel.

2. If the user selects a specific browser, for example:

`CHROME_HEADLESS`

The matrix becomes:

`["CHROME_HEADLESS"]`

and only one execution job is created.

### Benefits

- Centralized browser configuration
- Avoids hardcoding browser lists in workflows
- Easier maintenance if new browsers are added
- Reusable across multiple workflows

------------------------------------------------------------------------

## 🔹 Repository Secrets

No additional secrets are required.

`GITHUB_TOKEN`  is automatically provided by GitHub Actions.

------------------------------------------------------------------------

## 🏷 Tag Execution Strategy

The workflow dynamically determines which Cucumber tag to execute:

-   If the branch name contains a test case ID\
    Example:

    feature/TC-00004

    The workflow will execute:

    @TC-00004

-   Otherwise, it will execute the default tag defined in:

    SCENARIO_TAG

------------------------------------------------------------------------

## 🚀 Supported Execution Modes

This project supports multiple execution strategies:

- Pull Request execution (triggered when `review_start` label is added)
- Manual execution (`workflow_dispatch`)
- Scheduled execution (cron-based runs)

------------------------------------------------------------------------

## 📦 Reports

- Static analysis reports are uploaded as workflow artifacts
- Javadoc is generated and uploaded as an artifact
- Allure results are generated and can be deployed to `gh-pages`
