pipeline {
    agent any
    environment {
        dirName = 'foodbook-parent-pom'
    }
    stages {
        stage('Clean up') {
            steps {
                deleteDir()
            }
        }
        stage('Clone repo') {
            steps {
                sh 'git clone https://github.com/emstryszawski/foodbook-parent-pom.git'
            }
        }
        stage('Build') {
            steps {
                dir($dirName) {
                    sh 'mvn clean install'
                }
            }
        }
        stage('Package') {
            steps {
                dir($dirName) {
                    sh 'mvn -B package --file pom.xml'
                }
            }
        }
        stage('Deploy') {
            steps {
                dir($dirName) {
                    sh 'mvn deploy -s ${env.GITHUB_WORKSPACE}/settings.xml'
                }
            }
        }
    }
}