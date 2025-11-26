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
                dir('proto-back') {
                    sh './mvn clean package -DskipTests' 
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('proto-front') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'proto-back/target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'proto-front/build/**', fingerprint: true
            }
        }

        stage('Deploy to Integration VM') {
            steps {
                // Copier le backend et frontend 
                sh "scp proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                sh "scp -r proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"

                // Redémarrer le backend sur la VM
                sh "ssh ${SSH_USER}@${SSH_HOST} 'pkill -f java || true && nohup java -jar ${DEPLOY_DIR}/backend/*.jar > ${DEPLOY_DIR}/backend/logs.log 2>&1 &'"
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
