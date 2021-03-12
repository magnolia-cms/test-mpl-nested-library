/**
 * Example of using the custom nested pipeline
 */
def call(body) {
  // Init the MPL library
  MPLInit()

  // Executing the pipeline without additional configuration
  pipeline {  // Declarative pipeline
    agent {
      label 'master'
    }
    stages {

      // stage('Debug') { steps { echo "${CFG}" } }

      // TODO: get scripts and mount volume

      stage('verify build relevance') { steps { echo "verify build relevance" } }

      stage('build & deploy / build PR') {
        steps {
          MPLModule('Maven Build' /*, [ // Using overriden Maven Build
            maven: [
              tool_version: 'Maven 2'
            ]
          ]*/)
        }
      }

      stage('Binary compatibility') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule('BinaryCompatibility')
        }
      }

      stage('Dependency analysis') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule('DependencyAnalysis')
        }
      }

      stage('JIRA Health') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule('JiraHealth')
        }
      }

    }
  }
}
