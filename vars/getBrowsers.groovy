def call(String browser) {
    // Define the list of supported browsers
    def allBrowsers = [
            'CHROME_HEADLESS',
            'FIREFOX_HEADLESS',
            'EDGE_HEADLESS'
    ]
    return browser == 'ALL' ? allBrowsers : [browser]
}
