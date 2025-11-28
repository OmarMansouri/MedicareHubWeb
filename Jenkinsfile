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
                    // Only build, don't run the application
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
                    
                    // Copy new backend JAR
                    sh "scp Ing2-proto/Ing2-proto/proto-back/target/*.jar ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/backend/"
                    
                    // Copy frontend build
                    sh "scp -r Ing2-proto/Ing2-proto/proto-front/build/* ${SSH_USER}@${SSH_HOST}:${DEPLOY_DIR}/frontend/"
                    
                    // Start backend with proper database configuration
                    sh "ssh ${SSH_USER}@${SSH_HOST} 'cd ${DEPLOY_DIR}/backend && nohup java -jar *.jar --spring.profiles.active=prod > logs.log 2>&1 &'"
                }
            }
        }
    }
    
    post {
        failure {
            echo 'Pipeline failed - check database configuration and deployment scripts'
            emailext (
                subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Check console output at ${env.BUILD_URL}",
                to: "admin@example.com"
            )
        }
        success {
            echo 'Pipeline executed successfully!'
        }
    }
}
