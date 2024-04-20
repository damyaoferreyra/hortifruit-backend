pipeline {
    agent {
        docker {
            image 'docker:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    
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
