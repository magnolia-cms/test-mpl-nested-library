/**
 * Example of using the custom nested pipeline
 */
def call(body) {
  // Init the MPL library
  MPLInit()

  def MPL = MPLPipelineConfig(body, [
    cron: '',
    modules: [Build: [:], BinaryCompatibility: [:], DependencyAnalysis: [:], JiraHealth: [:]]
  ])

  // Executing the pipeline without additional configuration
  pipeline {  // Declarative pipeline
    agent {
      label 'master'
    }
    // triggers {
    //   cron MPL.cron
    // }
    stages {

      stage('Debug') { steps { echo "${MPL}" } }

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

      stage('BinaryCompatibility') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule()
        }
      }

      stage('DependencyAnalysis') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule()
        }
      }

      stage('JiraHealth') {
        when { expression { MPLModuleEnabled() } }
        steps {
          MPLModule()
        }
      }

    }
  }
}
