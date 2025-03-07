pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS = 'docker-hub-credentials'
        DOCKER_IMAGE = 'kaoutarsabbahi/imageprojet'
        CONTAINER_NAME = 'my_container'
        GIT_LFS_SKIP_SMUDGE = '1' // Set to bypass LFS smudge filter
    }

    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    // Ensure Git LFS is installed and skip smudge filter
                    bat 'git lfs install --skip-smudge'
                }
            }
        }

        stage('Checkout Source Code') {
            steps {
                // Checkout the source code with the environment variable set to skip LFS smudge filter
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true], [$class: 'CheckoutOption', timeout: 10]], 
                    submoduleCfg: [], 
                    userRemoteConfigs: [[url: 'https://github.com/Kaoutarsabbahi123/LastVersion.git']]
                ])
            }
        }

        stage('Build Application') {
            steps {
                dir('ProjectFinal') {
                    // Command to build the JAR file (ensure Maven is installed)
                    bat 'mvn clean package'
                }
            }
        }

        stage('Clean Up Old Containers') {
            steps {
                script {
                    // Remove any existing container named 'my_container'
                    bat '''
                    for /f "tokens=*" %%A in ('docker ps -a -q --filter "name=%CONTAINER_NAME%"') do (
                        docker rm -f %%A
                    )
                    exit 0
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build a new Docker image from the Dockerfile in the current directory
                    bat "docker build -t %DOCKER_IMAGE% ."
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    // Push the newly built Docker image to Docker Hub
                    withDockerRegistry(credentialsId: "${DOCKER_CREDENTIALS}", url: 'https://index.docker.io/v1/') {
                        docker.image(DOCKER_IMAGE).push('latest')
                    }
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Run a new container named 'my_container' using the latest image
                    bat "docker run -d -p 8080:80 --name %CONTAINER_NAME% %DOCKER_IMAGE%:latest"
                }
            }
        }

        stage('Clean Up Old Images') {
            steps {
                script {
                    // Remove old Docker images to save space
                    bat '''
                    for /f "tokens=*" %%I in ('docker images %DOCKER_IMAGE% -q') do (
                        docker rmi -f %%I
                    )
                    exit 0
                    '''
                }
            }
        }
    }

    post {
        always {
            // Clean up the workspace after the pipeline execution
            cleanWs()
        }
    }
}
