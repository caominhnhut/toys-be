pipeline {
    environment {
        DOCKER_HUB_REGISTRY = "nguyencaominhnhut/toys-be"
        DOCKER_HUB_CREDENTIALS = 'account_dockerhub'
    }

    agent any

    stages {

        stage("Build") {
            steps {
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }
        }
    }
}