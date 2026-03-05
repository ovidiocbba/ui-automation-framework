# Jenkins LTS with Java 17
# Base image already includes Jenkins and JDK 17
FROM jenkins/jenkins:lts-jdk17

# Switch to root to install system dependencies
USER root

RUN echo "===== JAVA VERSION =====" && java -version

# Install Base System Dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
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
    firefox-esr \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Configure UTF-8 locales
RUN locale-gen en_US.UTF-8

RUN echo "===== FIREFOX VERSION =====" && firefox --version

# Install Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub \
    | gpg --dearmor -o /usr/share/keyrings/google.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google.gpg] http://dl.google.com/linux/chrome/deb/ stable main" \
    > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends google-chrome-stable && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    echo "===== GOOGLE CHROME VERSION =====" && \
    google-chrome --version

# Install Microsoft Edge
RUN wget -q -O - https://packages.microsoft.com/keys/microsoft.asc \
    | gpg --dearmor -o /usr/share/keyrings/microsoft.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft.gpg] https://packages.microsoft.com/repos/edge stable main" \
    > /etc/apt/sources.list.d/microsoft-edge.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends microsoft-edge-stable && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    echo "===== MICROSOFT EDGE VERSION =====" && \
    microsoft-edge --version

# Install Edge WebDriver (required for Selenium)
# Edge does not include the WebDriver by default.
# The driver version must match the installed browser version to avoid compatibility errors.
RUN EDGE_VERSION=$(microsoft-edge --version | awk '{print $3}') && \
    echo "Installing EdgeDriver for version: $EDGE_VERSION" && \
    wget -q https://msedgedriver.microsoft.com/$EDGE_VERSION/edgedriver_linux64.zip && \
    unzip -q edgedriver_linux64.zip && \
    mv msedgedriver /usr/local/bin/ && \
    chmod +x /usr/local/bin/msedgedriver && \
    rm -f edgedriver_linux64.zip && \
    echo "===== EDGE DRIVER VERSION =====" && \
    msedgedriver --version

# Install Allure CLI
ENV ALLURE_VERSION=2.25.0

RUN wget -q https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.tgz && \
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
    blueocean

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

# Enable Jenkins Configuration as Code (JCasC)
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs

# Create configuration folder
RUN mkdir -p /var/jenkins_home/casc_configs

# Copy JCasC configuration file
COPY jenkins.yaml /var/jenkins_home/casc_configs/jenkins.yaml

# Fix permissions
RUN chown -R jenkins:jenkins /var/jenkins_home/casc_configs

# Set system language and encoding to English and UTF-8
ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8

# Set Java encoding to UTF-8 for Jenkins logs and operations
ENV JAVA_OPTS="-Dfile.encoding=UTF-8"

# Switch back to secure Jenkins user
USER jenkins
