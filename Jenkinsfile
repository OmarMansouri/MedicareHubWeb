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

        stage('Verify Endpoints') {
            steps {
                dir('Ing2-proto/Ing2-proto/proto-back/src/main/java/esiag/back/controllers') {
                    sh '''
                        echo "=== Vérification des contrôleurs ==="
                        if find . -name "*.java" -exec grep -l "@RestController\\|@Controller" {} \\; | grep -q "."; then
                            echo "✅ Contrôleurs Spring détectés"
                            echo "=== Endpoints trouvés ==="
                            grep -r "@GetMapping\\|@PostMapping\\|@RequestMapping" . || echo "⚠️ Aucun endpoint mapping trouvé"
                        else
                            echo "❌ AUCUN CONTRÔLEUR SPRING TROUVÉ"
                            echo "Ajoutez @RestController ou @Controller aux classes"
                            exit 1
                        fi
                    '''
                }
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
                // Nettoyer avant déploiement
                sh "ssh ${SSH_USER}@${SSH_HOST} 'rm -f ${DEPLOY_DIR}/backend/*.jar'"
                
                // Copier le backend et frontend 
                sh "scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                sh "scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"

                // Démarrer le serveur frontend
                sh """
                    ssh ${SSH_USER}@${SSH_HOST} '
                        cd ${DEPLOY_DIR}/frontend
                        pkill -f "serve.*3000" || true
                        npx serve -s . -l 3000 > frontend.log 2>&1 &
                        echo "Frontend démarré sur le port 3000"
                    '
                """

                // Redémarrer le backend CORRECTEMENT
                sh """
                    ssh ${SSH_USER}@${SSH_HOST} '
                        cd ${DEPLOY_DIR}/backend
                        pkill -f java || true
                        sleep 3
                        nohup java -jar *.jar --server.address=0.0.0.0 > logs.log 2>&1 &
                        echo "Backend démarré"
                        
                        # Attendre le démarrage
                        sleep 10
                        
                        # Vérifier que le backend répond
                        echo "=== Vérification du backend ==="
                        if curl -s http://localhost:8080 > /dev/null; then
                            echo "✅ Backend accessible en local"
                        else
                            echo "❌ Backend non accessible en local"
                        fi
                        
                        if curl -s http://172.31.250.86:8080 > /dev/null; then
                            echo "✅ Backend accessible depuis l\\'extérieur"
                        else
                            echo "❌ Backend non accessible depuis l\\'extérieur"
                        fi
                    '
                """
            }
        }

        stage('Health Check') {
            steps {
                sh """
                    echo "=== Test final des endpoints ==="
                    sleep 5
                    curl -s http://${SSH_HOST}:8080/ || echo "Endpoint racine: 404"
                    curl -s http://${SSH_HOST}:3000/ && echo "Frontend: OK" || echo "Frontend: Erreur"
                    
                    echo "=== URLs de l'application ==="
                    echo "Frontend: http://${SSH_HOST}:3000"
                    echo "Backend:  http://${SSH_HOST}:8080"
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline exécuté avec succès !'
            echo 'Frontend: http://172.31.250.86:3000'
            echo 'Backend:  http://172.31.250.86:8080'
        }
        failure {
            echo 'Échec du pipeline.'
        }
    }
}