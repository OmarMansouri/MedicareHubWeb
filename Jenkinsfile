pipeline {
    agent any

    environment {
        SSH_HOST = '172.31.250.86'       // IP de la VM d'intégration
        SSH_USER = 'medicare'            // utilisateur SSH
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
                echo 'Transfert du backend et frontend vers la VM...'
                sh "rsync -avz Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                sh "rsync -avz Ing2-proto/Ing2-proto/proto-front/build/ ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"

                echo 'Redémarrage des services sur la VM...'
                sh """
                ssh ${SSH_USER}@${SSH_HOST} '
                    set +e
                    # Stopper les services existants (ignorer les erreurs)
                    pkill -f java
                    pkill -f serve

                    # Lancer backend et frontend
                    nohup java -jar ${DEPLOY_DIR}/backend/*.jar > ${DEPLOY_DIR}/backend/logs.log 2>&1 &
                    nohup serve -s ${DEPLOY_DIR}/frontend > ${DEPLOY_DIR}/frontend/logs.log 2>&1 &
                '
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline exécuté avec succès ! Backend et Frontend sont déployés.'
        }
        failure {
            echo 'Échec du pipeline. Vérifie les logs.'
        }
    }
}
