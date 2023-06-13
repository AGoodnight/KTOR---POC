pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building KTOR server'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing KTOR'
            }
        }
        stage ('Deploy') {
            steps {
                echo "Deploy KTOR to Lambda"
            }
        }
    }
}
