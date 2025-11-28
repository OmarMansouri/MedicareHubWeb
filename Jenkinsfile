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
                dir('Ing2-proto/Ing2-proto/proto-back') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Ing2-proto/Ing2-proto/proto-front') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'Ing2-proto/Ing2-proto/proto-back/target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'Ing2-proto/Ing2-proto/proto-front/build/**', fingerprint: true
            }
        }

        stage('Deploy to Integration VM') {
            steps {

                // Copier backend + frontend
                sh "scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                sh "scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"

                // Une seule commande : restart backend + lancer frontend
                sh """
                ssh ${SSH_USER}@${SSH_HOST} '
                    pkill -f java || true

                    echo "Lancement du backend..."
                    nohup java -jar ${DEPLOY_DIR}/backend/*.jar \
                        > ${DEPLOY_DIR}/backend/logs.log 2>&1 &

                    echo "Lancement du frontend..."
                    cd ${DEPLOY_DIR}/frontend &&
                    nohup npx serve -s . -l 3000 \
                        > ${DEPLOY_DIR}/frontend/logs.log 2>&1 &

                    echo "Déploiement terminé."
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
