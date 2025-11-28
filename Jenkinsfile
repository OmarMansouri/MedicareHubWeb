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
                git branch: 'main', 
                url: 'https://github.com/OmarMansouri/MedicareHubWeb.git'
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
                    // Stop existing backend
                    sh "ssh ${SSH_USER}@${SSH_HOST} 'pkill -f java || true'"
                    
                    // Create directories if they don't exist
                    sh "ssh ${SSH_USER}@${SSH_HOST} 'mkdir -p ${DEPLOY_DIR}/backend ${DEPLOY_DIR}/frontend'"
                    
                    // Copy backend JAR
                    sh "scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                    
                    // Copy frontend build
                    sh "scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"
                    
                    // Start backend
                    sh "ssh ${SSH_USER}@${SSH_HOST} 'cd ${DEPLOY_DIR}/backend && nohup java -jar *.jar > logs.log 2>&1 &'"
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    // Wait for backend to start
                    sh """
                        for i in {1..10}; do
                            if ssh ${SSH_USER}@${SSH_HOST} 'curl -s http://localhost:8080/actuator/health > /dev/null'; then
                                echo "Backend is healthy!"
                                break
                            fi
                            echo "Waiting for backend to start... attempt \$i"
                            sleep 10
                        done
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
        }
        success {
            echo 'Pipeline executed successfully!'
            // Optional: Send success notification
        }
        failure {
            echo 'Pipeline failed!'
            // Optional: Send failure notification
        }
    }
}
