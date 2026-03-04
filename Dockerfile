# Jenkins LTS with Java 17
FROM jenkins/jenkins:lts-jdk17

# Switch to root to install plugins
USER root

RUN echo "===== JAVA VERSION =====" && java -version

# Install Base System Dependencies
RUN apt-get update && apt-get install -y \
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
    && rm -rf /var/lib/apt/lists/*

# Install Firefox
RUN apt-get update && apt-get install -y \
    firefox-esr \
    && rm -rf /var/lib/apt/lists/* \
    && echo "===== FIREFOX VERSION =====" \
    && firefox --version

# Install Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor > /usr/share/keyrings/google.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/* && \
    echo "===== GOOGLE CHROME VERSION =====" && \
    google-chrome --version

# Install Microsoft Edge
RUN wget -q -O - https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > /usr/share/keyrings/microsoft.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft.gpg] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge.list && \
    apt-get update && \
    apt-get install -y microsoft-edge-stable && \
    rm -rf /var/lib/apt/lists/* && \
    echo "===== MICROSOFT EDGE VERSION =====" && \
    microsoft-edge --version

# Install Allure CLI
ENV ALLURE_VERSION=2.25.0

RUN wget https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.tgz && \
    tar -zxvf allure-${ALLURE_VERSION}.tgz -C /opt/ && \
    ln -s /opt/allure-${ALLURE_VERSION}/bin/allure /usr/bin/allure && \
    rm allure-${ALLURE_VERSION}.tgz && \
    echo "===== ALLURE VERSION =====" && \
    allure --version

# Install required plugins:
# - workflow-aggregator → Enables Pipeline (Jenkinsfile support)
# - pipeline-stage-view → Visual stage view in UI
# - git → Allows Jenkins to clone repositories
# - credentials-binding → Secure usage of secrets in pipelines
# - allure-jenkins-plugin → Integrates Allure test reports
# - configuration-as-code → Allows Jenkins to be configured via YAML files (JCasC),
#   enabling Infrastructure as Code and eliminating manual UI configuration
# - job-dsl → Enables creation of Jenkins jobs via code (Job DSL),
#   allowing automatic pipeline creation without manual UI configuration
# Install Jenkins Plugins
RUN jenkins-plugin-cli --plugins \
    workflow-aggregator \
    pipeline-stage-view \
    git \
    credentials-binding \
    allure-jenkins-plugin \
    configuration-as-code \
    job-dsl

# Enable Jenkins Configuration as Code
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs

# Create configuration folder
RUN mkdir -p /var/jenkins_home/casc_configs

# Copy JCasC configuration file
COPY jenkins.yaml /var/jenkins_home/casc_configs/jenkins.yaml

# Fix permissions
RUN chown -R jenkins:jenkins /var/jenkins_home/casc_configs

# Switch back to default secure user
USER jenkins
