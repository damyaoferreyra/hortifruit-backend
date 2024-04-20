pipeline {
    agent any
    
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    def dockerImageName = "hortifruit-be:${env.BUILD_ID}"
                    sh "docker-compose -f docker-compose.yml build ${dockerImageName}"
                }
            }
        }
    }
}
