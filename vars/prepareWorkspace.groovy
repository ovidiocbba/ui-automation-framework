def call() {
    cleanWs()
    // Define the list of supported browsers inside a script block
    def ALL_BROWSERS = ['CHROME_HEADLESS', 'FIREFOX_HEADLESS', 'EDGE_HEADLESS']
    return params.BROWSER == 'ALL' ? ALL_BROWSERS : [params.BROWSER]
}
