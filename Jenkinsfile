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
                    sh 'ls -la target/*.jar'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Ing2-proto/Ing2-proto/proto-front') {
                    sh 'npm install'
                    sh 'npm run build'
                    sh 'ls -la build/'
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
                script {
                    sh """
                        scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/
                    """
                    
                    sh """
                        scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/
                    """

                    sh """
                        ssh ${SSH_USER}@${SSH_HOST} '
                            cd ${DEPLOY_DIR}/backend
                            
                            # Arrêter l'application existante
                            pkill -f "java.*jar" || true
                            sleep 3
                            
                            # Vérifier le JAR à exécuter
                            JAR_FILE=\$(ls *.jar | head -1)
                            echo "Démarrage du JAR: \$JAR_FILE"
                            
                            # Démarrer la nouvelle version
                            nohup java -jar \$JAR_FILE --server.port=8080 > backend.log 2>&1 &
                            echo "Backend démarre sur le port 8080..."
                            
                            # Attendre et vérifier le processus
                            sleep 5
                            if pgrep -f "java.*jar" > /dev/null; then
                                echo "✅ Backend démarré avec succès"
                                echo "PID: \$(pgrep -f "java.*jar")"
                            else
                                echo "❌ Échec du démarrage du backend"
                                # Afficher les logs en cas d'échec
                                tail -20 backend.log || true
                                exit 1
                            fi
                        '
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sleep 15
                    
                    sh """
                        echo "Vérification du backend..."
                        curl -s -o /dev/null -w "%{http_code}" http://${SSH_HOST}:8080/actuator/health || echo "Backend non accessible"
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline exécuté avec succès !'
            echo "Backend accessible sur: http://${SSH_HOST}:8080"
            echo "Frontend accessible sur: http://${SSH_HOST}:3000 (ou le port configuré)"
        }
        failure {
            echo '❌ Échec du pipeline.'
        }
        always {
            
        }
    }
}
