def call(browsers, params, gradleFlags) {

    def branch = sh(
            script: "git branch --show-current || git name-rev --name-only HEAD",
            returnStdout: true
    ).trim()

    def commit = sh(
            script: "git rev-parse HEAD",
            returnStdout: true
    ).trim()

    // Centralized Gradle parameters to avoid duplication
    def commonParams = "-Dcucumber.filter.tags=${params.SCENARIO_TAG} " +
            "-DbaseUrl=${params.BASE_URL} " +
            "-DexplicitWait=${params.EXPLICIT_WAIT} " +
            "-Dthreads=${params.THREADS} " +
            "-Dbranch=${branch} " +
            "-Dcommit=${commit}"

    // Map that will store parallel stages
    def parallelStages = [:]

    // Loop through each selected browser
    for (browser in browsers) {

        def selectedBrowser = browser

        // Isolated build directory per browser
        // Prevents parallel executions from overwriting the shared "build/" folder
        // Ensures independent Gradle outputs and Allure results
        def browserBuildDir = "build-${selectedBrowser}"

        // Centralized browser-specific parameters
        def browserParams = "-Dbrowser=${selectedBrowser} " +
                "-Dallure.results.directory=${browserBuildDir}/allure-results " +
                "-Dorg.gradle.project.buildDir=${browserBuildDir} " +
                "${gradleFlags}"

        // Create a parallel stage per browser
        parallelStages[selectedBrowser] = {

            stage("Run ${selectedBrowser}") {

                try {
                    // Execute main test task
                    sh "./gradlew executeFeatures ${commonParams} ${browserParams}"
                } catch (err) {
                    echo "Re-running failed scenarios for ${selectedBrowser}"

                    try {
                        // If execution fails, re-run failed scenarios only
                        sh "./gradlew reExecuteFeatures ${commonParams} ${browserParams}"
                        echo "Retry succeeded for ${selectedBrowser}"
                    } catch (retryErr) {
                        echo "Retry failed for ${selectedBrowser}"
                        throw retryErr
                    }
                }

                // Save logs for this specific browser
                archiveArtifacts artifacts: "build/logs/${selectedBrowser}/*.log", allowEmptyArchive: true

                // Save allure results for debugging if needed
                archiveArtifacts artifacts: "${browserBuildDir}/allure-results/**", allowEmptyArchive: true
            }
        }
    }

    // failFast: false ensures one browser failure does NOT stop the others
    parallelStages.failFast = false
    // Run all browser stages in parallel
    parallel parallelStages
}
