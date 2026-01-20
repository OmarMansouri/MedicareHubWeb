pipeline {
    agent any

    environment {
        SSH_HOST = '172.31.250.86'
        SSH_USER = 'medicare'
        DEPLOY_DIR = '/home/medicare/medicareapp'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/OmarMansouri/MedicareHubWeb.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('Medicare-back') {
                   
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Medicare-front') {
                    sh 'npm install'
                    sh 'CI= npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'Medicare-back/target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'Medicare-front/build/**', fingerprint: true
            }
        }

        stage('Deploy to Integration VM') {
            steps {
                sh """
                    ssh ${SSH_USER}@${SSH_HOST} '
                        mkdir -p ${DEPLOY_DIR}/backend
                        mkdir -p ${DEPLOY_DIR}/frontend
                    '
                """

                sh "scp Medicare-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                sh "scp -r Medicare-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"

                sh """
                    ssh ${SSH_USER}@${SSH_HOST} '
                        cd ${DEPLOY_DIR}
                        chmod +x run.sh
                        ./run.sh
                    '
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline exécuté avec succès !'
        }
        failure {
            echo 'Échec du pipeline.'
        }
    }
}