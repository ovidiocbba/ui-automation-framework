pipeline {
    // Run this pipeline on any available Jenkins agent
    agent any

    options {
        // Stop the build if it runs more than 30 minutes
        timeout(time: 30, unit: 'MINUTES')
        // Do not allow two builds of this job at the same time
        disableConcurrentBuilds()
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
    }

    stages {

        // Clean workspace before build
        stage('Prepare Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                // Clone repository from SCM (Git)
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Compile project but skip tests
                sh './gradlew clean build -x test --no-daemon --stacktrace'
            }
        }

        stage('Static Analysis') {
            steps {
                // Run Checkstyle and PMD
                sh './gradlew staticAnalysis --no-daemon --stacktrace'
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
            steps {

                // Added to allow pipeline continuation even if tests fail
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {

                    script {
                        // Centralized Gradle parameters to avoid duplication
                        def commonParams = "-Dcucumber.filter.tags=${params.SCENARIO_TAG} " +
                                           "-DbaseUrl=${params.BASE_URL} " +
                                           "-DexplicitWait=${params.EXPLICIT_WAIT} " +
                                           "-Dthreads=${params.THREADS}"

                        // Centralized Gradle flags to avoid repeating daemon and stacktrace options
                        def gradleFlags = "--no-daemon --stacktrace"

                        // Set browsers for parallel execution based on user input
                        def browsers = params.BROWSER == 'ALL'
                            ? ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS']
                            : [params.BROWSER]
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
                                    // Save logs for this specific browser
                                    archiveArtifacts artifacts: "${browserBuildDir}/logs/**/*.log", allowEmptyArchive: true

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
            steps {
                script {
                    // Check generated files dynamically based on selected browser(s)
                    def browsers = params.BROWSER == 'ALL'
                        ? ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS']
                        : [params.BROWSER]

                    for (browser in browsers) {
                        sh "ls -R build-${browser}/allure-results || true"
                    }
                }
            }
        }

        stage('Allure Report') {
            steps {
                script {
                    def resultPaths = []

                    if (params.BROWSER == 'ALL') {
                        resultPaths = [
                            [path: 'build-CHROME_HEADLESS/allure-results'],
                            [path: 'build-FIREFOX_HEADLESS/allure-results'],
                            [path: 'build-EDGE_HEADLESS/allure-results']
                        ]
                    } else {
                        resultPaths = [
                            [path: "build-${params.BROWSER}/allure-results"]
                        ]
                    }

                    // Check if the results directory exists before generating the report
                    sh "echo 'Checking Allure results directory existence...'"
                    sh "ls -R ${resultPaths.collect { it.path }.join(' ')} || true"

                    // Generate the Allure report
                    sh "allure generate ${resultPaths.collect { it.path }.join(' ')} --clean -o allure-report"
                }
            }
        }

        // Generate index.html to show links to the Allure reports for each browser
        stage('Generate Index') {
            steps {
                script {
                    echo "Running index generation script..."
                    sh '''
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
                // All CSS, JS, and subreport directories are preserved
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

    post {
        always {
            // This ensures the pipeline completes cleanly and reports remain available
            echo "Pipeline finished. Reports are preserved even if tests fail."

            // Archive the generated Allure report for additional access
            archiveArtifacts artifacts: 'allure-report/**', allowEmptyArchive: true
        }
    }
}
