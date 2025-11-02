// Jenkinsfile - CI/CD Spring Boot Secure Pipeline (Declarative)

pipeline {
  agent { label 'docker' }         // or 'ubuntu' / any node with docker, maven, java, awscli, kubectl installed
  options {
    ansiColor('xterm')
    timeout(time: 60, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }

  environment {
    // map to Jenkins credentials below using withCredentials where needed
    AWS_REGION        = "us-east-1"
    APP_NAME          = "java-springboot-app"
    // ECR repo uri will be constructed after AWS_ACCOUNT_ID is available via credentials
    // SHORT_SHA will be set in a script step
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
        script {
          env.GIT_COMMIT = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
          env.SHORT_SHA = env.GIT_COMMIT ? env.GIT_COMMIT.substring(0,7) : sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
          echo "SHORT_SHA=${env.SHORT_SHA}"
        }
      }
    }

    stage('Setup JDK & Clear Maven Cache') {
      tools { jdk 'jdk17' } // configure in Jenkins global tools as 'jdk17'
      steps {
        sh '''
          echo "Clearing old Maven cache for spring/tomcat..."
          rm -rf ~/.m2/repository/org/springframework || true
          rm -rf ~/.m2/repository/org/apache/tomcat || true
          pwd; ls -la
        '''
      }
    }

    stage('TruffleHog Secret Scan') {
      steps {
        sh '''
          echo "Installing trufflehog..."
          wget -q https://github.com/trufflesecurity/trufflehog/releases/download/v3.90.11/trufflehog_3.90.11_linux_amd64.tar.gz -O trufflehog.tar.gz
          tar -xzf trufflehog.tar.gz
          chmod +x trufflehog
          mkdir -p $HOME/.local/bin
          mv trufflehog $HOME/.local/bin/
          export PATH=$HOME/.local/bin:$PATH
          echo "Running TruffleHog secret scan..."
          trufflehog filesystem . > scan_output.log 2>&1 || true
          if grep -q "Secrets detected!" scan_output.log ; then
            echo "Secrets detected! Please remove hardcoded credentials or API keys."
            cat scan_output.log
            exit 1
          else
            echo "No secrets found."
            cat scan_output.log
          fi
        '''
      }
    }

    stage('Build & Test (Maven)') {
      steps {
        sh 'mvn clean compile test verify -B'
        // Archive test reports
        junit '**/target/surefire-reports/*.xml'
        archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
      }
    }

    stage('SonarCloud Analysis (JaCoCo)') {
      environment {
        SONAR_TOKEN = credentials('sonar-token')   // create Jenkins credential id 'sonar-token' (Secret Text)
      }
      steps {
        sh '''
          SONAR_SCANNER_VERSION=6.2.1.4610
          wget -q https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux-x64.zip
          unzip -q sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux-x64.zip
          export PATH=$PATH:$(pwd)/sonar-scanner-${SONAR_SCANNER_VERSION}-linux-x64/bin
          sonar-scanner -X \
            -Dsonar.projectKey=rootpromptnext_java-springboot-app \
            -Dsonar.organization=rootpromptnext \
            -Dsonar.host.url=https://sonarcloud.io \
            -Dsonar.login=${SONAR_TOKEN} \
            -Dsonar.sources=src/main/java \
            -Dsonar.tests=src/test/java \
            -Dsonar.java.binaries=target/classes \
            -Dsonar.junit.reportPaths=target/surefire-reports \
            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml || true
        '''
      }
    }

    stage('Snyk SCA') {
      environment {
        SNYK_TOKEN = credentials('snyk-token')     // secret text
      }
      steps {
        sh '''
          wget -O snyk https://github.com/snyk/cli/releases/download/v1.1294.3/snyk-linux
          chmod +x snyk
          sudo mv snyk /usr/local/bin/snyk || mv snyk /usr/local/bin/snyk
          snyk auth ${SNYK_TOKEN}
          snyk monitor --all-projects || true
        '''
      }
      post {
        always {
          archiveArtifacts artifacts: 'snyk*', allowEmptyArchive: true
        }
      }
    }

    stage('OWASP Dependency-Check') {
      environment {
        NVD_API_KEY = credentials('nvd-api-key')          // secret text
        OSSINDEX_USERNAME = credentials('ossindex-user') // username/password can also be used
        OSSINDEX_PASSWORD = credentials('ossindex-pass')
      }
      steps {
        sh '''
          wget -q https://github.com/jeremylong/DependencyCheck/releases/download/v12.1.0/dependency-check-12.1.0-release.zip
          unzip -q dependency-check-12.1.0-release.zip -d $HOME/dependency-check
          chmod +x $HOME/dependency-check/dependency-check/bin/dependency-check.sh
          mkdir -p dependency-check-report
          $HOME/dependency-check/dependency-check/bin/dependency-check.sh \
            --project "${APP_NAME}" \
            --scan . \
            --format HTML \
            --out dependency-check-report \
            --nvdApiKey ${NVD_API_KEY} \
            --ossIndexUser ${OSSINDEX_USERNAME} \
            --ossIndexPassword ${OSSINDEX_PASSWORD} || true
        '''
      }
      post {
        always {
          archiveArtifacts artifacts: 'dependency-check-report/**', fingerprint: true
        }
      }
    }

    stage('Rename JAR for Docker') {
      steps {
        sh '''
          mv target/*-SNAPSHOT.jar target/app.jar || mv target/*.jar target/app.jar || true
          ls -l target || true
        '''
        archiveArtifacts artifacts: 'target/app.jar', allowEmptyArchive: true
      }
    }

    stage('Build Docker images (GHCR) & Scan') {
      environment {
        GHCR_TOKEN = credentials('ghcr-token')   // secret text - Personal Access Token with packages:write
        GHCR_USER  = 'rootpromptnext'            // change if needed
      }
      steps {
        sh '''
          echo "${GHCR_TOKEN}" | docker login ghcr.io -u ${GHCR_USER} --password-stdin
          docker build -t ghcr.io/${GHCR_USER}/${APP_NAME}:${SHORT_SHA} -t ghcr.io/${GHCR_USER}/${APP_NAME}:latest .
        '''
      }
      post {
        always {
          archiveArtifacts artifacts: 'trivy-ghcr-report.json', allowEmptyArchive: true
        }
      }
    }

    stage('Install Trivy') {
      steps {
        sh '''
          wget https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh -O trivy-install.sh
          bash trivy-install.sh
          mkdir -p $HOME/.local/bin
          mv ./bin/trivy $HOME/.local/bin/ || true
          export PATH=$HOME/.local/bin:$PATH
          trivy --version || true
        '''
      }
    }

    stage('Trivy Scan GHCR image') {
      steps {
        sh '''
          trivy image --format json --output trivy-ghcr-report.json --severity CRITICAL,HIGH ghcr.io/${GHCR_USER}/${APP_NAME}:${SHORT_SHA} || true
        '''
        archiveArtifacts artifacts: 'trivy-ghcr-report.json', allowEmptyArchive: true
      }
    }

    stage('Push GHCR images') {
      steps {
        sh '''
          docker push ghcr.io/${GHCR_USER}/${APP_NAME}:${SHORT_SHA} || true
          docker push ghcr.io/${GHCR_USER}/${APP_NAME}:latest || true
        '''
      }
    }

    stage('Build Docker images (Docker Hub) & Scan & Push') {
      environment {
        DOCKERHUB_USER = credentials('dockerhub-user')   // username stored as "Username with Password" type is fine
        DOCKERHUB_PASS = credentials('dockerhub-pass')   // password
      }
      steps {
        sh '''
          docker build -t ${DOCKERHUB_USER}/${APP_NAME}:${SHORT_SHA} -t ${DOCKERHUB_USER}/${APP_NAME}:latest .
          echo "${DOCKERHUB_PASS}" | docker login -u ${DOCKERHUB_USER} --password-stdin
          trivy image --format json --output trivy-dockerhub-report.json --severity CRITICAL,HIGH ${DOCKERHUB_USER}/${APP_NAME}:${SHORT_SHA} || true
          docker push ${DOCKERHUB_USER}/${APP_NAME}:${SHORT_SHA} || true
          docker push ${DOCKERHUB_USER}/${APP_NAME}:latest || true
        '''
        archiveArtifacts artifacts: 'trivy-dockerhub-report.json', allowEmptyArchive: true
      }
    }

    stage('Build Docker image (ECR) & Push & Scan') {
      environment {
        AWS_ACCOUNT_ID = credentials('aws-account-id')   // secret text (or username/password style); used only to form ECR URI
        AWS_ACCESS_KEY_ID = credentials('aws-access-key')     // AWS credentials: use 'Username with password' or 'AWS Credentials' plugin
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-key')
      }
      steps {
        script {
          ECR_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${APP_NAME}"
          env.ECR_URI = ECR_URI
        }
        // configure AWS CLI using env creds
        withEnv(["AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}", "AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}", "AWS_DEFAULT_REGION=${AWS_REGION}"]) {
          sh '''
            aws ecr describe-repositories --repository-names ${APP_NAME} || aws ecr create-repository --repository-name ${APP_NAME} || true
            docker build -t ${ECR_URI}:${SHORT_SHA} -t ${ECR_URI}:latest .
            aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_URI}
            trivy image --format json --output trivy-ecr-report.json --severity CRITICAL,HIGH ${ECR_URI}:${SHORT_SHA} || true
            docker push ${ECR_URI}:${SHORT_SHA} || true
            docker push ${ECR_URI}:latest || true
          '''
          archiveArtifacts artifacts: 'trivy-ecr-report.json', allowEmptyArchive: true
        }
      }
    }

    stage('Start Spring Boot App (for DAST)') {
      steps {
        sh '''
          nohup java -jar target/app.jar --server.port=8090 > app.log 2>&1 &
          sleep 15
          curl -I http://localhost:8090 || (echo "App failed to start" && cat app.log && exit 1)
        '''
      }
    }

    stage('OWASP ZAP DAST Scan') {
      steps {
        // Use official ZAP Docker image to run active scan against local app
        sh '''
          echo "Launching ZAP baseline/active scan..."
          docker run --rm -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-stable zap-baseline.py -t http://host.docker.internal:8090 -r zap-report.html || true
          # If host.docker.internal is unavailable on your Jenkins node, use the host network variant:
          # docker run --rm --network host -t owasp/zap2docker-stable zap-baseline.py -t http://localhost:8090 -r zap-report.html || true
        '''
        archiveArtifacts artifacts: 'zap-report.html', allowEmptyArchive: true
      }
    }

    stage('Update kubeconfig & Deploy to EKS') {
      environment {
        KUBE_CLUSTER = "java-springboot-eks"
      }
      steps {
        withEnv(["AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}", "AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}", "AWS_DEFAULT_REGION=${AWS_REGION}"]) {
          sh '''
            aws eks update-kubeconfig --region ${AWS_REGION} --name ${KUBE_CLUSTER}
            sed -i "s|{{IMAGE}}|${ECR_URI}:${SHORT_SHA}|g" deployment.yaml || true
            sed -i "s|{{APP_NAME}}|${APP_NAME}|g" deployment.yaml || true
            echo "Final manifest:"
            cat deployment.yaml || true
            kubectl apply -f deployment.yaml
            kubectl rollout status deployment/${APP_NAME} -n default --timeout=180s
            kubectl get svc ${APP_NAME} -n default -o wide || true
          '''
        }
      }
    }

  } // stages

  post {
    success {
      echo "Pipeline succeeded: ${currentBuild.fullDisplayName}"
    }
    failure {
      echo "Pipeline failed: ${currentBuild.fullDisplayName}"
    }
    always {
      archiveArtifacts artifacts: 'app.log,scan_output.log,dependency-check-report/**,trivy-*.json,zap-report.html', allowEmptyArchive: true
    }
  }
}
