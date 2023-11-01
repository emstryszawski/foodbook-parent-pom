pipeline {
    agent any
    environment {
        DIR_NAME = 'foodbook-parent-pom'
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
                dir(env.DIR_NAME) {
                    sh 'mvn clean install'
                }
            }
        }
        stage('Package') {
            steps {
                dir(env.DIR_NAME) {
                    sh 'mvn -B package --file pom.xml'
                }
            }
        }
        stage('Deploy') {
            steps {
                dir(env.DIR_NAME) {
                    sh 'mvn deploy'
                }
            }
        }
    }
}