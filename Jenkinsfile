pipeline {
    agent any

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
        choice(name: 'ENV', choices: ['qa', 'dev', 'stage', 'prod'], description: 'Environment')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run in headless mode')
        string(name: 'TAGS', defaultValue: '@smoke', description: 'Cucumber tag expression')
    }

    tools {
        jdk 'jdk-17'
        maven 'maven-3'
    }

    stages {
        stage('Test') {
            steps {
                sh """
                    mvn clean test \
                      -Dbrowser=${params.BROWSER} \
                      -Denv=${params.ENV} \
                      -Dheadless=${params.HEADLESS} \
                      -Dcucumber.filter.tags="${params.TAGS}"
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/cucumber-reports/**', allowEmptyArchive: true
            junit allowEmptyResults: true, testResults: 'target/cucumber-reports/cucumber.xml'
        }
    }
}
