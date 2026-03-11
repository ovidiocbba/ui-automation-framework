pipeline {
    // Run this pipeline on any available Jenkins agent
    agent any

    options {
        // Disable Jenkins automatic checkout to avoid cloning the repository twice
        skipDefaultCheckout(true)

        // Allows tools like Gradle to display colored output (errors, warnings, info)
        ansiColor('xterm')

        // Global timeout to stop the whole pipeline if it runs too long
        timeout(time: 30, unit: 'MINUTES')

        // Do not allow two builds of this job at the same time
        disableConcurrentBuilds()

        // Add timestamps to Jenkins logs for better debugging
        timestamps()

        // Keep only the latest 20 builds and artifacts from the last 10 builds to save disk space
        buildDiscarder(logRotator(
            numToKeepStr: '20',
            artifactNumToKeepStr: '10'
        ))
    }

    parameters {
        // Browser selection parameter
        choice(
            name: 'BROWSER',
            choices: ['ALL', 'CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS'],
            description: 'Browser to execute (ALL | CHROME_HEADLESS | FIREFOX_HEADLESS | EDGE_HEADLESS)'
        )
        string(name: 'SCENARIO_TAG', defaultValue: '@regression', description: 'Scenario tag to execute (e.g. "@TC-00001")')
        string(name: 'BASE_URL', defaultValue: 'https://www.amazon.com', description: 'Base URL (e.g. "https://www.amazon.com")')
        string(name: 'EXPLICIT_WAIT', defaultValue: '30', description: 'Explicit wait timeout in seconds (e.g. "30")')
        string(name: 'THREADS', defaultValue: '2', description: 'Number of parallel threads for test execution (e.g. "2")')
    }

    environment {
        // Use JDK configured in Jenkins Global Tool Configuration
        JAVA_HOME = tool name: 'jdk17'
        // Add selected JDK to system PATH
        PATH = "${JAVA_HOME}/bin:${env.PATH}"

        // Shared Gradle cache to make builds faster (improves CI performance)
        GRADLE_USER_HOME = "/var/jenkins_home/.gradle"

        // Gradle flags used in all pipeline executions
        // --no-daemon: do not start Gradle daemon in CI
        // --stacktrace: show full error stack trace
        // --info: show more details in the build logs
        // --warning-mode all: show all Gradle warnings
        GRADLE_FLAGS = "--no-daemon --stacktrace --info --warning-mode all"

        // Centralizing list of supported browsers (defined globally for access in other stages)
        ALL_BROWSERS = ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS']
    }

    stages {
        // Clean workspace before build (Gradle cache is kept outside)
        stage('Prepare Workspace') {
            steps {
                cleanWs()
                script {
                    // Define the list of supported browsers inside a script block
                    def ALL_BROWSERS = ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS']
                    browsers = params.BROWSER == 'ALL' ? ALL_BROWSERS : [params.BROWSER]
                }
            }
        }

        stage('Checkout') {
            steps {
                // Clone repository from Git (retry helps avoid temporary network errors)
                retry(3) {
                    checkout scm
                }
            }
        }

        stage('Build') {
            steps {
                // Clean workspace and compile project, skipping tests.
                sh "./gradlew clean assemble ${env.GRADLE_FLAGS}"
            }
        }

        stage('Static Analysis') {
            steps {
                // Run Checkstyle and PMD
                sh "./gradlew staticAnalysis ${env.GRADLE_FLAGS}"
            }
            post {
                always {
                    // Save static analysis reports as Jenkins artifacts
                    archiveArtifacts artifacts: 'build/reports/**', allowEmptyArchive: true
                }
            }
        }

        stage('Javadoc') {
            steps {
                // Generate Javadoc documentation
                sh './gradlew javadoc'
            }
            post {
                always {
                    // Save generated Javadoc
                    archiveArtifacts artifacts: 'build/docs/javadoc/**', allowEmptyArchive: true
                }
            }
        }

        stage('Execute Tests') {

            // Stage timeout to stop this stage if tests take too long
            options {
                timeout(time: 20, unit: 'MINUTES')
            }

            steps {
                // Added to allow pipeline continuation even if tests fail
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    script {
                        // Centralized Gradle parameters to avoid duplication
                        def commonParams = "-Dcucumber.filter.tags=${params.SCENARIO_TAG} " +
                                           "-DbaseUrl=${params.BASE_URL} " +
                                           "-DexplicitWait=${params.EXPLICIT_WAIT} " +
                                           "-Dthreads=${params.THREADS}"

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
                                                "${env.GRADLE_FLAGS}"

                            // Create a parallel stage per browser
                            parallelStages[selectedBrowser] = {
                                stage("Run ${selectedBrowser}") {
                                    try {
                                        // Execute main test task
                                        sh """
                                            ./gradlew executeFeatures \
                                            ${commonParams} \
                                            ${browserParams}
                                        """
                                    } catch (err) {
                                        // If execution fails, re-run failed scenarios only
                                        echo "Re-running failed scenarios for ${selectedBrowser}"
                                        try {
                                            sh """
                                                ./gradlew reExecuteFeatures \
                                                ${commonParams} \
                                                ${browserParams}
                                            """
                                            echo "Retry succeeded for ${selectedBrowser}"
                                        } catch (retryErr) {
                                            echo "Retry failed for ${selectedBrowser}"
                                            throw retryErr
                                        }
                                    }
                                    // DEBUG: Check if logs exist before archiving
                                    echo "Listing the log files for ${selectedBrowser}..."
                                    sh """
                                        find build/logs/${selectedBrowser} -name "*.log" || echo "No logs found."
                                    """

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
                }
            }
        }

        // Debugging step to check if allure-results directories are being generated
        stage('Debug') {
            when {
                expression { currentBuild.currentResult == 'FAILURE' }
            }
            steps {
                script {
                    echo "Checking allure results for browsers"
                    for (browser in browsers) {
                        echo "Checking allure results for ${browser}"
                        sh "ls -R build-${browser}/allure-results || true"
                    }
                }
            }
        }

        stage('Allure Report') {
            steps {
                script {
                    def resultPaths = browsers.collect { browser ->
                        "build-${browser}/allure-results"
                    }

                    // Check if the results directory exists before generating the report
                    sh "echo 'Checking Allure results directory existence...'"
                    sh "ls -R ${resultPaths.join(' ')} || true"
                    // Generate the Allure report
                    sh "allure generate ${resultPaths.join(' ')} --clean -o allure-report"
                }
            }
        }

        // Generate index.html to show links to the Allure reports for each browser
        stage('Generate Index') {
            steps {
                script {
                    echo "Running index generation script..."
                    sh '''
                        set -e
                        chmod +x jenkins/scripts/generate-index.sh
                        ./jenkins/scripts/generate-index.sh
                        echo "Listing contents of the allure-report directory:"
                        ls -l allure-report/
                    '''
                }
            }
        }

        stage('Publish Allure Report') {
            steps {
                // Publish the full allure-report folder
                publishHTML(target: [
                    reportDir: 'allure-report',       // root folder containing index.html + subreports
                    reportFiles: 'index.html',        // main entry point
                    reportName: 'Report', // display name in Jenkins (no spaces)
                    keepAll: true,                    // keep old builds
                    alwaysLinkToLastBuild: true       // link to latest build
                ])
            }
        }
    }
}
