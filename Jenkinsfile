pipeline {
    agent any
    
    environment {
        DOCKER_CREDENTIALS = 'docker-hub-credentials'
        DOCKER_IMAGE = 'kaoutarsabbahi/imageprojet'
        CONTAINER_NAME = 'my_container'
    }
    
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    withDockerRegistry(credentialsId: "${DOCKER_CREDENTIALS}", url: 'https://index.docker.io/v1/') {
                        docker.image(DOCKER_IMAGE).push('latest')
                    }
                }
            }
        }

        stage('Clean Up Old Images') {
            steps {
                script {
                    bat """
                    for /f "skip=1 delims=" %%I in ('docker images --filter=reference^=${DOCKER_IMAGE} --format "{{.ID}}"') do docker rmi -f %%I
                    """
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    bat """
                    for /f "tokens=*" %%A in ('docker ps -a -q --filter "name=${CONTAINER_NAME}"') do docker rm -f %%A
                   
                    docker run -d --name ${CONTAINER_NAME} ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }

        stage('Clean Up Old Containers') {
            steps {
                script {
                    bat """
                    for /f "skip=1 tokens=*" %%A in ('docker ps -a -q --filter "name=${CONTAINER_NAME}"') do docker rm -f %%A
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
