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
                // Copier le backend et frontend 
                sh "scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                sh "scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"


                // Redémarrer le backend sur la VM
                sh """
                    ssh ${SSH_USER}@${SSH_HOST} << 'EOF'
                    cd ${DEPLOY_DIR}/backend
                    pkill -f proto-back || true
                    sleep 2
                    nohup java -jar proto-back-1.0-SNAPSHOT.jar > logs.log 2>&1 &
                    sleep 5
                    echo "Backend démarré"
                EOF
                """
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
