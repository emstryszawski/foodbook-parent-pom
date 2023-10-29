pipeline {
    agent any
    stages {
        stage('Clean up') {
            steps {
                deleteDir()
            }
        }
        stage('Clone repo') {
            steps {
                sh 'git clone https://github.com/emstryszawski/foodbook-api.git'
            }
        }
        stage('Build') {
            steps {
                dir('foodbook-api') {
                    sh 'mvn clean install'
                    sh 'mvn -B package --file pom.xml'
                }
            }
        }
        stage('Deploy') {
            steps {
                dir('foodbook-api') {
                    sh 'mvn deploy -s ${env.GITHUB_WORKSPACE}/settings.xml'
                }
            }
        }
    }
}