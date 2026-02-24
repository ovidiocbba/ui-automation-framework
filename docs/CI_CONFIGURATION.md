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

| Name            | Example Value              | Description                               |
|-----------------|----------------------------|-------------------------------------------|
| `BASE_URL`      | `https://www.amazon.com`   | Base URL used during test execution       |
| `EXPLICIT_WAIT` | `30`                       | Explicit wait timeout (in seconds)        |
| `SCENARIO_TAG`  | `@regression`              | Default Cucumber tag for execution        |
------------------------------------------------------------------------

## 🔹 Repository Secrets

No additional secrets are required.

GITHUB_TOKEN is automatically provided by GitHub Actions.

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

-   Pull Request execution (triggered when `review_start` label is
    added)
-   Manual execution (`workflow_dispatch`)
-   Scheduled execution (cron-based runs)

------------------------------------------------------------------------

## 📦 Reports

-   Static analysis reports are uploaded as workflow artifacts
-   Javadoc is generated and uploaded as an artifact
-   Allure results are generated and can be deployed to `gh-pages`
