/**
 * Example of using the custom nested pipeline
 */
def call(body) {
  // Init the MPL library
  MPLInit()

  def MPL = MPLPipelineConfig(body, [modules: [Build: [:], BinaryCompatibility: [:], DependencyAnalysis: [:], JiraHealth: [:]]])

  // Executing the pipeline without additional configuration
  pipeline {  // Declarative pipeline
    agent {
      label 'master'
    }
    stages {

      stage('Debug') { steps { echo "${MPL.moduleEnabled('DependencyAnalysis')}" } }

      // TODO: get scripts and mount volume

      stage('verify build relevance') { steps { echo "verify build relevance" } }

      stage('build & deploy / build PR') {
        steps {
          MPLModule('Maven Build', [
            maven: [
              tool_version: 'Maven 3'
            ]
          ])
        }
      }

      stage('Binary compatibility') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule()
        }
      }

      stage('Dependency analysis') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule()
        }
      }

      stage('JIRA Health') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule()
        }
      }

    }
  }
}
