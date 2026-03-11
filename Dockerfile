# Jenkins LTS with Java 17
# Base image already includes Jenkins and JDK 17
FROM jenkins/jenkins:2.541.2-lts-jdk17

# Switch to root to install system dependencies
USER root

RUN echo "===== JAVA VERSION =====" && java -version

# Install Base System Dependencies, locales and UTF-8 support
ENV DEBIAN_FRONTEND=noninteractive

# First update and install all dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    wget \
    gnupg \
    unzip \
    rsync \
    ca-certificates \
    fonts-liberation \
    libnss3 \
    libxss1 \
    libasound2 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libgbm1 \
    xvfb \
    xauth \
    git \
    firefox-esr \
    locales && \
    echo "en_US.UTF-8 UTF-8" > /etc/locale.gen && \
    locale-gen && \
    update-locale LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8 && \
    echo "===== GIT VERSION =====" && git --version && \
    echo "===== FIREFOX VERSION =====" && firefox --version

# Set system language and encoding to English and UTF-8
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

# Check if Google Chrome is installed, if not, install it
RUN if ! dpkg -l | grep -q google-chrome-stable; then \
    curl -fsSL https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable; \
    echo "Google Chrome installed."; \
    else \
    echo "Google Chrome is already installed. Skipping installation."; \
fi && \
echo "===== GOOGLE CHROME VERSION =====" && google-chrome-stable --version

# Install Microsoft Edge (stable from repo) and EdgeDriver for Selenium
RUN curl -fsSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor -o /usr/share/keyrings/microsoft.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft.gpg] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends microsoft-edge-stable && \
    # Get Microsoft Edge version
    EDGE_VERSION=$(microsoft-edge --version | awk '{print $3}') && \
    echo "===== MICROSOFT EDGE VERSION =====" && \
    echo "Installed Microsoft Edge version: $EDGE_VERSION" && \
    # Install EdgeDriver (matching the Edge version)
    if curl -fsSL "https://msedgedriver.microsoft.com/$EDGE_VERSION/edgedriver_linux64.zip" -o edgedriver_linux64.zip; then \
        unzip -q edgedriver_linux64.zip && \
        mv msedgedriver /usr/local/bin/ && \
        chmod +x /usr/local/bin/msedgedriver && \
        rm -f edgedriver_linux64.zip && \
        echo "===== EDGEDRIVER VERSION =====" && msedgedriver --version && \
        echo "EdgeDriver installed."; \
    else \
        echo "Matching EdgeDriver not found, using latest release"; \
        EDGE_VERSION=$(curl -fsSL https://msedgedriver.microsoft.com/LATEST_RELEASE) && \
        curl -fsSL "https://msedgedriver.microsoft.com/$EDGE_VERSION/edgedriver_linux64.zip" -o edgedriver_linux64.zip && \
        unzip -q edgedriver_linux64.zip && \
        mv msedgedriver /usr/local/bin/ && \
        chmod +x /usr/local/bin/msedgedriver && \
        rm -f edgedriver_linux64.zip && \
        echo "===== EDGEDRIVER VERSION =====" && msedgedriver --version && \
        echo "EdgeDriver latest version installed."; \
    fi

# Install Allure CLI
ENV ALLURE_VERSION=2.25.0

RUN curl -fsSL https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.tgz -o allure-${ALLURE_VERSION}.tgz && \
    tar -zxf allure-${ALLURE_VERSION}.tgz -C /opt/ && \
    ln -s /opt/allure-${ALLURE_VERSION}/bin/allure /usr/bin/allure && \
    rm -f allure-${ALLURE_VERSION}.tgz && \
    echo "===== ALLURE VERSION =====" && \
    allure --version

# Install Jenkins Plugins
RUN jenkins-plugin-cli --plugins \
    workflow-aggregator \
    pipeline-stage-view \
    git \
    credentials-binding \
    allure-jenkins-plugin \
    configuration-as-code \
    job-dsl \
    blueocean \
    ansicolor \
    timestamper \
    ws-cleanup \
    htmlpublisher

# Plugins:
# - workflow-aggregator → Enables Pipeline (Jenkinsfile support)
# - pipeline-stage-view → Visual stage view in UI
# - git → Allows Jenkins to clone repositories
# - credentials-binding → Secure usage of secrets in pipelines
# - allure-jenkins-plugin → Integrates Allure test reports
# - configuration-as-code → Allows Jenkins to be configured via YAML files (JCasC),
#   enabling Infrastructure as Code and eliminating manual UI configuration
# - job-dsl → Enables creation of Jenkins jobs via code (Job DSL),
# - blueocean → Provides a modern, user-friendly UI for Jenkins with enhanced pipeline visualization
# - ansicolor → Enables ANSI color support in Jenkins console logs
# - timestamper → Adds timestamps to each line of the Jenkins console output
# - ws-cleanup → Allows cleaning the workspace in pipelines using cleanWs()
# - htmlpublisher → Publishes HTML reports in Jenkins (used for Allure report UI)

# Enable Jenkins Configuration as Code (JCasC)
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs

# Create configuration folder and copy JCasC configuration file
RUN mkdir -p /var/jenkins_home/casc_configs && \
    chown -R jenkins:jenkins /var/jenkins_home/casc_configs

# Copy JCasC configuration file
COPY jenkins.yaml /var/jenkins_home/casc_configs/jenkins.yaml

# Set Java encoding to UTF-8 for Jenkins logs and disable Jenkins Content Security Policy so HTML reports like Allure can load JS/CSS
ENV JAVA_OPTS="-Dfile.encoding=UTF-8 -Dhudson.model.DirectoryBrowserSupport.CSP="

# Clean up apt cache and lists
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Switch back to secure Jenkins user
USER jenkins
