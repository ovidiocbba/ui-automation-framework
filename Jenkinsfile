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
            choices: ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS', 'ALL'],
            description: 'Browser to execute'
        )
        string(name: 'SCENARIO_TAG', defaultValue: '@regression', description: 'Cucumber tag')
        string(name: 'BASE_URL', defaultValue: 'https://www.amazon.com', description: 'Base URL')
        string(name: 'EXPLICIT_WAIT', defaultValue: '30', description: 'Explicit wait (seconds)')
        string(name: 'THREADS', defaultValue: '2', description: 'Parallel threads')
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
                sh './gradlew clean build -x test'
            }
        }

        stage('Static Analysis') {
            steps {
                // Run Checkstyle and PMD
                sh './gradlew staticAnalysis'
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
                    def browsers = []

                    // If user selects ALL, run all three browsers
                    if (params.BROWSER == 'ALL') {
                        browsers = ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS']
                    }
                    // Otherwise run only selected browser
                    else {
                        browsers = [params.BROWSER]
                    }
                    // Map that will store parallel stages
                    def parallelStages = [:]
                    // Loop through each selected browser
                    for (browser in browsers) {
                        def selectedBrowser = browser
                        // Create a parallel stage per browser
                        parallelStages[selectedBrowser] = {
                            stage("Run ${selectedBrowser}") {
                                try {
                                    // Execute main test task
                                    sh """
                                        mkdir -p build/logs
                                        ./gradlew clean executeFeatures \
                                          -Dcucumber.filter.tags="${params.SCENARIO_TAG}" \
                                          -Dbrowser="${selectedBrowser}" \
                                          -DbaseUrl="${params.BASE_URL}" \
                                          -DexplicitWait="${params.EXPLICIT_WAIT}" \
                                          -Dthreads="${params.THREADS}" \
                                          -Dallure.results.directory=build/allure-results/${selectedBrowser} \
                                          | tee build/logs/${selectedBrowser}.log
                                    """
                                } catch (err) {
                                    // If execution fails, re-run failed scenarios only
                                    echo "Re-running failed scenarios for ${selectedBrowser}"

                                    sh """
                                        ./gradlew reExecuteFeatures \
                                          -Dbrowser="${selectedBrowser}" \
                                          -DbaseUrl="${params.BASE_URL}" \
                                          -DexplicitWait="${params.EXPLICIT_WAIT}" \
                                          -Dthreads="${params.THREADS}" \
                                          -Dallure.results.directory=build/allure-results/${selectedBrowser} \
                                          | tee build/logs/${selectedBrowser}-rerun.log
                                    """
                                    // Mark stage as failed after re-run
                                    throw err
                                }
                                // Save logs for this specific browser
                                archiveArtifacts artifacts: "build/logs/${selectedBrowser}*.log",
                                                  allowEmptyArchive: true
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

        stage('Allure Report') {
            steps {
                script {
                    def resultPaths = []
                    if (params.BROWSER == 'ALL') {
                        resultPaths = [
                            [path: 'build/allure-results/CHROME_HEADLESS'],
                            [path: 'build/allure-results/FIREFOX_HEADLESS'],
                            [path: 'build/allure-results/EDGE_HEADLESS']
                        ]
                    } else {
                        resultPaths = [
                            [path: "build/allure-results/${params.BROWSER}"]
                        ]
                    }
                    allure([
                        results: resultPaths
                    ])
                }
            }
        }
    }
}
