pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = "rootpromptnext"
        IMAGE_NAME = "java-springboot-app1"

        BUILD_TAG = "${BUILD_NUMBER}"
        LATEST_TAG = "latest"

        FULL_IMAGE = "${DOCKERHUB_USERNAME}/${IMAGE_NAME}"
    }

    stages {

        // =========================
        // Checkout
        // =========================
        stage('Checkout Repo') {
            steps {
                git url: 'https://github.com/rootpromptnext/java-springboot-app', branch: 'main'
            }
        }

        // =========================
        // Build App
        // =========================
        stage('Build Maven App (Docker)') {
            steps {
                sh '''
                docker run --rm \
                  -u $(id -u):$(id -g) \
                  -v $PWD:/app \
                  -w /app \
                  maven:3.9.9-eclipse-temurin-17 \
                  mvn clean package -DskipTests
                '''
            }
        }

        // =========================
        // Build Docker Image
        // =========================
        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t $FULL_IMAGE:$BUILD_TAG .
                docker tag $FULL_IMAGE:$BUILD_TAG $FULL_IMAGE:$LATEST_TAG
                '''
            }
        }

        // =========================
        // Login to Docker Hub
        // =========================
        stage('Login to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-token',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    '''
                }
            }
        }

        // =========================
        // Push Image
        // =========================
        stage('Push to Docker Hub') {
            steps {
                sh '''
                docker push $FULL_IMAGE:$BUILD_TAG
                docker push $FULL_IMAGE:$LATEST_TAG
                '''
            }
        }

        // =========================
        // Update Deployment
        // =========================
        stage('Update deployment.yaml') {
            steps {
                sh '''
                sed -i "s|image:.*|image: $FULL_IMAGE:$BUILD_TAG|g" deployment.yaml
                '''
            }
        }

        // =========================
        // Deploy to Kubernetes
        // =========================
        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    sh '''
                    export KUBECONFIG=$KUBECONFIG
                    kubectl apply -f deployment.yaml
                    '''
                }
            }
        }
    }

    // =========================
    // SIMPLE POST
    // =========================
    post {
        always {
            echo "Pipeline completed: ${JOB_NAME} #${BUILD_NUMBER}"
        }
    }
}
