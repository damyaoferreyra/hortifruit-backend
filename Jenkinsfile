pipeline {
    agent any
    
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    dockerapp = docker.build("fpsoluctionstechs/hortifruit-be", "-f dockerfile .")
                }
            }
        }
    }
}
