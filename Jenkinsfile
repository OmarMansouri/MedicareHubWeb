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
        script {
            // Stop existing services
            sh "ssh ${SSH_USER}@${SSH_HOST} 'pkill -f java || true'"
            
            // Copy backend
            sh "scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
            
            // Copy frontend
            sh "scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"
            
            // Update frontend configuration for production
            sh """
                ssh ${SSH_USER}@${SSH_HOST} "
                    # Update frontend config to point to backend
                    sed -i 's|http://localhost:8080|http://${SSH_HOST}:8080|g' ${DEPLOY_DIR}/frontend/assets/environment.js
                    
                    # Start backend
                    cd ${DEPLOY_DIR}/backend && nohup java -jar *.jar > backend.log 2>&1 &
                    
                    # Start frontend server (if using Node.js server)
                    # cd ${DEPLOY_DIR}/frontend && nohup npx serve -s . -p 3000 > frontend.log 2>&1 &
                "
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
