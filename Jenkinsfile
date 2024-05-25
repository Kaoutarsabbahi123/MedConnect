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
                    docker.build(DOCKER_IMAGE)
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS) {
                        docker.image(DOCKER_IMAGE).push('latest')
                    }
                }
            }
        }

        stage('Clean Up Old Images') {
            steps {
                script {
                    sh '''
                    docker images --filter=reference=$DOCKER_IMAGE --format "{{.ID}}" | tail -n +2 | xargs -r docker rmi
                    '''
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    sh '''
                    # Supprimer les anciens conteneurs s'ils existent
                    docker ps -a -q --filter "name=$CONTAINER_NAME" | xargs -r docker rm -f
                    # Exécuter un nouveau conteneur
                    docker run -d --name $CONTAINER_NAME $DOCKER_IMAGE:latest
                    '''
                }
            }
        }

        stage('Clean Up Old Containers') {
            steps {
                script {
                    // Supprimer tous les conteneurs sauf le plus récent
                    sh '''
                    docker ps -a -q --filter "name=$CONTAINER_NAME" | tail -n +2 | xargs -r docker rm -f
                    '''
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

