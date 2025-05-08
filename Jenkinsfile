pipeline {
    agent any

    environment {
        IMAGE_NAME = 'imhrkr/imhr-backend'
        IMAGE_TAG  = "1.${BUILD_NUMBER}"
    }

    stages {

         stage('Clean up directory') {
            steps {
                script {
                    sh 'rm -rf ./* ./.git'
                }
            }
        }

        stage('Clone Repository') {
            steps {
                // Git 리포지토리 클론
                echo "Cloning Repository..."
                git branch: 'main', url: 'https://github.com/beyond-sw-camp/be12-fin-HRIM-IMHR-BE.git'
            }
        }

        stage('Check cloned files') {
            steps {
                sh 'ls -al'
            }
        }

        stage('Check gradle wrapper') {
            steps {
                sh 'ls -al gradle/wrapper'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew bootJar'
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'IMHR_DOCKER_HUB', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Edit Manifest') {
            steps {
                script{
                    echo "Editing Manifest..."
                    sh 'cd ..'
                    sh 'git checkout -f deploy/argo/cd'

                    sh """
                    cd deploy
                    sed -i 's/:1\\.[0-9]\\+/:${IMAGE_TAG}/g' backend-rollout.yml
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                script{
                    echo "Deploying Components..."
                    withCredentials([
                           usernamePassword(credentialsId: 'IUCH_GIT', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_PASSWORD'),
//                            string(credentialsId: 'github_username', variable: 'GITHUB_USERNAME'),
//                            string(credentialsId: 'github_token', variable: 'GITHUB_TOKEN'),
                           string(credentialsId: 'IMHR_GIT_URL', variable: 'GITHUB_URL')
                       ]) {
                          sh """
                          git add .
                          git commit -m "[Deploy] v1.${BUILD_NUMBER} 배포"
                          echo "after commit"
                          git remote set-url origin https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${GITHUB_URL}
                          echo "after seturl"
                          git push origin deploy/argo/cd
                          """
                    }
                }
            }
        }
    }
}
