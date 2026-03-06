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
                                        ./gradlew clean executeFeatures \
                                        ${commonParams} \
                                        ${browserParams}
                                    """
                                } catch (err) {
                                    // If execution fails, re-run failed scenarios only
                                    echo "Re-running failed scenarios for ${selectedBrowser}"

                                    sh """
                                        ./gradlew reExecuteFeatures \
                                        ${commonParams} \
                                        ${browserParams}
                                    """
                                    // Mark stage as failed after re-run
                                    throw err
                                }
                                // Save logs for this specific browser
                                archiveArtifacts artifacts: "build/logs/**/*.log", allowEmptyArchive: true
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

        // Debugging step to check if allure-results directories are being generated
        stage('Debug') {
            steps {
                script {
                    // Verificar archivos generados
                    sh 'ls -R build-CHROME_HEADLESS/allure-results'
                    sh 'ls -R build-FIREFOX_HEADLESS/allure-results'
                    sh 'ls -R build-EDGE_HEADLESS/allure-results'
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
                    sh "ls -R ${resultPaths.collect { it.path }.join(' ')}"

                    // Generate the Allure report
                    sh "allure generate ${resultPaths.collect { it.path }.join(' ')} --clean -o allure-report"
                }
            }
        }

        // Generate index.html to show links to the Allure reports for each browser
        stage('Generate Index') {
            steps {
                script {
                    sh """
                        #!/bin/bash
                        echo "Generating index.html..."

                        OUTPUT="allure-report/index.html"

                        cat <<EOF > "$OUTPUT"
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <meta charset="UTF-8">
                        <title>UI Automation Framework - Allure Reports</title>
                        <style>
                        body {
                          font-family: Arial, sans-serif;
                          text-align: center;
                          background-color: #0f172a;
                          color: white;
                          margin-top: 60px;
                        }
                        h1 { margin-bottom: 40px; }
                        .container {
                          display: flex;
                          justify-content: center;
                          gap: 30px;
                          flex-wrap: wrap;
                        }
                        .card {
                          background: #1e293b;
                          padding: 30px;
                          border-radius: 12px;
                          width: 220px;
                          transition: 0.3s;
                        }
                        .card:hover {
                          background: #334155;
                          transform: translateY(-5px);
                        }
                        a {
                          color: #38bdf8;
                          text-decoration: none;
                          font-size: 18px;
                          font-weight: bold;
                        }
                        </style>
                        </head>
                        <body>
                        <h1>Allure Test Reports</h1>
                        <div class="container">
                        EOF

                    for dir in allure-report/allure-*; do
                        [ -d "$dir" ] || continue
                        name=$(basename "$dir")
                        browser=${name#allure-}

                        cat <<EOF >> "$OUTPUT"
                        <div class="card">
                          <a href="./$name/index.html">${browser^} Report</a>
                        </div>
                        EOF
                    done

                    cat <<EOF >> "$OUTPUT"
                    </div>
                    </body>
                    </html>
                    EOF

                    echo "index.html generated successfully."
                    """
                }
            }
        }

        // Publish Allure Report as HTML in Jenkins UI
        stage('Publish Allure Report') {
            steps {
                // Publish the Allure report using Jenkins HTML Publisher plugin
                publishHTML(target: [
                    reportDir: 'allure-report',
                    reportFiles: 'index.html',
                    reportName: 'Allure Test Report'
                ])
            }
        }
    }

    post {
        always {
            script {
                // Final attempt to generate Allure Report to ensure visibility in Jenkins UI
                def allureResults = [
                    [path: 'build-CHROME_HEADLESS/allure-results'],
                    [path: 'build-FIREFOX_HEADLESS/allure-results'],
                    [path: 'build-EDGE_HEADLESS/allure-results']
                ]
                allure commandline: 'allure', results: allureResults
            }
        }
    }
}
