def call(browsers) {

    def resultPaths = browsers.collect { browser ->
        "build-${browser}/allure-results"
    }

    echo "Checking Allure results directories..."
    sh "ls -R ${resultPaths.join(' ')} || true"

    echo "Generating Allure report..."
    sh "allure generate ${resultPaths.join(' ')} --clean -o allure-report"

    // Generate index.html to show links to the Allure reports for each browser
    echo "Generating report index..."
    sh '''
        set -e
        chmod +x jenkins/scripts/generate-index.sh
        ./jenkins/scripts/generate-index.sh
    '''

    echo "Listing contents of the allure-report directory:"
    sh "ls -l allure-report/"
}