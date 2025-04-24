pipeline {
    agent any

    environment {
        IMAGE_NAME = 'imhrkr/imhr-backend:1.'
        IMAGE_TAG  = "${BUILD_NUMBER}"
    }

    stages {

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
                    sh 'git checkout deploy/argo/cd'

                    sh """
                    cd deploy
                    sed -i 's/:LATEST/:1.${BUILD_NUMBER}/g' backend-rollout.yml
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
                          git remote set-url origin https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${GITHUB_URL}
                          # 변경 사항을 원격 저장소의 cicd 브랜치로 푸시
                          git push origin deploy/argo/cd
                          """
                    }
                }
            }
        }
    }
}
